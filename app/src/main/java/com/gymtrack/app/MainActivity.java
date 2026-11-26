package com.gymtrack.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    private static final int BG = 0xff0F1217, MUTED = 0xff9AA7B8,
            BLUE_LIGHT = 0xff80A8FF, GREEN = 0xff32D7A3, AMBER = 0xffF5C36A;
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private SharedPreferences prefs;
    private String token;
    private JSONObject perfil, membresia;
    private JSONArray planes;
    private int tab = 0;
    private boolean loading;
    private FrameLayout container;
    private View bottomNav;
    private TextView[] navItems;

    private interface Work<T> { T run() throws Exception; }
    private interface Done<T> { void accept(T result); }
    private interface Fail { void accept(Exception e); }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        getWindow().getDecorView().setSystemUiVisibility(0);
        setContentView(R.layout.activity_main);
        View root = findViewById(R.id.main);
        root.setOnApplyWindowInsetsListener((v, insets) -> {
            v.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
            return insets.consumeSystemWindowInsets();
        });
        root.requestApplyInsets();
        container = findViewById(R.id.screen_container);
        bottomNav = findViewById(R.id.bottom_nav);
        navItems = new TextView[]{findViewById(R.id.nav_perfil),
                findViewById(R.id.nav_membresia), findViewById(R.id.nav_planes)};
        for (int i = 0; i < navItems.length; i++) {
            final int index = i;
            navItems[i].setOnClickListener(v -> { tab = index; showDashboard(); });
        }
        prefs = getSharedPreferences("gymtrack_session", MODE_PRIVATE);
        Object retained = getLastNonConfigurationInstance();
        token = retained instanceof String ? (String) retained : prefs.getString("token", null);
        tab = state == null ? 0 : state.getInt("tab", 0);
        if (token == null) showLogin(""); else refresh();
    }

    @Override protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        out.putInt("tab", tab);
    }
    @Override public Object onRetainNonConfigurationInstance() { return token; }
    @Override protected void onDestroy() { io.shutdownNow(); super.onDestroy(); }

    // Todas las vistas se cargan desde res/layout; no se crean a mano.
    private View openScreen(int layout, boolean showNav) {
        container.removeAllViews();
        View screen = getLayoutInflater().inflate(layout, container, false);
        container.addView(screen);
        bottomNav.setVisibility(showNav ? View.VISIBLE : View.GONE);
        return screen;
    }

    private <T> void async(Work<T> work, Done<T> done, Fail fail) {
        io.execute(() -> {
            try {
                T result = work.run();
                runOnUiThread(() -> { if (!isDestroyed() && !isFinishing()) done.accept(result); });
            } catch (Exception e) {
                runOnUiThread(() -> { if (!isDestroyed() && !isFinishing()) fail.accept(e); });
            }
        });
    }

    private String message(Exception e) {
        if (e instanceof Api.ErrorHttp) return e.getMessage();
        if (e instanceof java.io.IOException)
            return "No se pudo conectar con GymTrack. Revisa el servidor y tu conexión.";
        return "No se pudieron procesar los datos. Intenta nuevamente.";
    }

    private void showLogin(String error) {
        loading = false;
        View screen = openScreen(R.layout.activity_login, false);
        EditText correo = screen.findViewById(R.id.login_correo);
        EditText contra = screen.findViewById(R.id.login_contra);
        CheckBox remember = screen.findViewById(R.id.login_remember);
        TextView loginError = screen.findViewById(R.id.login_error);
        Button ingresar = screen.findViewById(R.id.btn_ingresar);
        loginError.setText(error);
        loginError.setVisibility(error.isEmpty() ? View.GONE : View.VISIBLE);

        ingresar.setOnClickListener(v -> {
            if (loading) return;
            String email = correo.getText().toString().trim();
            String password = contra.getText().toString();
            boolean save = remember.isChecked();
            if (email.isEmpty() || password.isEmpty()) {
                loginError.setText("Escribe tu correo y contraseña.");
                loginError.setVisibility(View.VISIBLE);
                return;
            }
            InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (keyboard != null) keyboard.hideSoftInputFromWindow(contra.getWindowToken(), 0);
            loading = true;
            ingresar.setText("Ingresando…");
            ingresar.setEnabled(false);
            loginError.setVisibility(View.GONE);
            async(() -> Api.login(email, password, save), t -> {
                token = t;
                contra.setText("");
                if (save) prefs.edit().putString("token", t).apply();
                else prefs.edit().remove("token").apply();
                tab = 0;
                loading = false;
                refresh();
            }, e -> {
                loading = false;
                ingresar.setEnabled(true);
                ingresar.setText("Ingresar");
                loginError.setText(message(e));
                loginError.setVisibility(View.VISIBLE);
            });
        });
    }

    private static class Data { JSONObject perfil, membresia; JSONArray planes; }

    private void refresh() {
        if (token == null) { showLogin(""); return; }
        if (loading) return;
        loading = true;
        View screen = openScreen(R.layout.view_estado, false);
        setText(screen, R.id.estado_titulo, "Consultando GymTrack…");
        String current = token;
        async(() -> {
            Data d = new Data();
            d.perfil = Api.perfil(current);
            d.membresia = Api.membresia(current);
            d.planes = Api.planes(current);
            return d;
        }, d -> {
            loading = false;
            perfil = d.perfil;
            membresia = d.membresia;
            planes = d.planes;
            showDashboard();
        }, e -> {
            loading = false;
            if (!expired(e)) showConnectionError(message(e));
        });
    }

    private boolean expired(Exception e) {
        if (e instanceof Api.ErrorHttp && ((Api.ErrorHttp)e).code == 401) {
            clearSession();
            showLogin("Tu sesión terminó. Inicia sesión nuevamente.");
            return true;
        }
        return false;
    }

    private void showConnectionError(String error) {
        View screen = openScreen(R.layout.view_estado, false);
        screen.findViewById(R.id.estado_progress).setVisibility(View.GONE);
        setText(screen, R.id.estado_titulo, "No se pudieron cargar tus datos");
        setText(screen, R.id.estado_mensaje, error);
        screen.findViewById(R.id.estado_mensaje).setVisibility(View.VISIBLE);
        View retry = screen.findViewById(R.id.estado_reintentar);
        retry.setVisibility(View.VISIBLE);
        retry.setOnClickListener(v -> refresh());
        View exit = screen.findViewById(R.id.estado_logout);
        exit.setVisibility(View.VISIBLE);
        exit.setOnClickListener(v -> logout());
    }

    private void showDashboard() {
        if (perfil == null || membresia == null || planes == null) return;
        if (tab == 0) showProfile();
        else if (tab == 1) showMembership();
        else showPlans();
        for (int i = 0; i < navItems.length; i++) {
            int color = i == tab ? BLUE_LIGHT : MUTED;
            navItems[i].setTextColor(color);
            navItems[i].setCompoundDrawableTintList(ColorStateList.valueOf(color));
            navItems[i].setSelected(i == tab);
        }
    }

    private void bindRefresh(View screen) {
        screen.findViewById(R.id.btn_refresh).setOnClickListener(v -> refresh());
    }

    private void showProfile() {
        View screen = openScreen(R.layout.fragment_perfil, true);
        bindRefresh(screen);
        String name = display(perfil, "nombre");
        setText(screen, R.id.perfil_avatar, initials(name));
        setText(screen, R.id.perfil_nombre, name);
        boolean active = "ACTIVO".equals(value(perfil, "estatus"));
        setPill(screen, R.id.perfil_estado, active ? "Socio activo" : "Socio inactivo", active);
        setText(screen, R.id.perfil_id, display(perfil, "id"));
        setText(screen, R.id.perfil_tel, display(perfil, "telefono"));
        setText(screen, R.id.perfil_correo, display(perfil, "correo"));
        setText(screen, R.id.perfil_primera, date(value(perfil, "primeraMembresia")));
        screen.findViewById(R.id.btn_editar).setOnClickListener(v -> editProfile());
        screen.findViewById(R.id.btn_logout).setOnClickListener(v -> logout());
    }

    private void showMembership() {
        View screen = openScreen(R.layout.fragment_membresia, true);
        bindRefresh(screen);
        String status = value(membresia, "estado");
        boolean active = "VIGENTE".equals(status);
        String label;
        switch (status) {
            case "VIGENTE": label = "Vigente"; break;
            case "VENCIDA": label = "Vencida"; break;
            case "CANCELADA": label = "Cancelada"; break;
            case "PENDIENTE": label = "Por iniciar"; break;
            default: label = "Sin membresía";
        }
        setPill(screen, R.id.membresia_estado, label, active);
        String plan = value(membresia, "plan");
        setText(screen, R.id.membresia_plan,
                plan.isEmpty() ? "No tienes una membresía registrada" : plan);
        boolean hasDates = !value(membresia, "fin").isEmpty();
        screen.findViewById(R.id.membresia_detalles).setVisibility(hasDates ? View.VISIBLE : View.GONE);
        if (hasDates) {
            setText(screen, R.id.membresia_inicio, "Inicio: " + date(value(membresia, "inicio")));
            setText(screen, R.id.membresia_fin, "Vencimiento: " + date(value(membresia, "fin")));
            setText(screen, R.id.membresia_precio, money(membresia, "precioCobrado"));
        }
        screen.findViewById(R.id.membresia_dias).setVisibility(active ? View.VISIBLE : View.GONE);
        if (active) {
            long days = membresia.optLong("diasRestantes");
            setText(screen, R.id.membresia_dias, days == 0 ? "Vence hoy" :
                    days + (days == 1 ? " día restante" : " días restantes"));
        }
        setText(screen, R.id.membresia_aviso,
                "Consulta los planes disponibles para contratar o renovar en recepción.");
        screen.findViewById(R.id.membresia_aviso).setVisibility(active ? View.GONE : View.VISIBLE);
        boolean inactiveAccount = active && !"ACTIVO".equals(value(perfil, "estatus"));
        screen.findViewById(R.id.membresia_inactiva).setVisibility(inactiveAccount ? View.VISIBLE : View.GONE);
        screen.findViewById(R.id.btn_explorar).setOnClickListener(v -> { tab = 2; showDashboard(); });
    }

    private void showPlans() {
        View screen = openScreen(R.layout.fragment_planes, true);
        bindRefresh(screen);
        LinearLayout list = screen.findViewById(R.id.lista_planes);
        int count = 0;
        for (int i = 0; i < planes.length(); i++) {
            JSONObject plan = planes.optJSONObject(i);
            if (plan == null) continue;
            // Se reutiliza la misma tarjeta XML por cada plan devuelto por MySQL.
            View item = getLayoutInflater().inflate(R.layout.item_plan, list, false);
            setText(item, R.id.plan_nombre, display(plan, "nombre"));
            setText(item, R.id.plan_precio, money(plan, "precio"));
            int days = plan.optInt("duracionDias");
            setText(item, R.id.plan_duracion, days + (days == 1 ? " día de duración" : " días de duración"));
            list.addView(item);
            count++;
        }
        screen.findViewById(R.id.planes_vacio).setVisibility(count == 0 ? View.VISIBLE : View.GONE);
    }

    private void editProfile() {
        if (loading) return;
        View form = getLayoutInflater().inflate(R.layout.dialog_editar_perfil, null);
        EditText name = form.findViewById(R.id.editar_nombre);
        EditText mail = form.findViewById(R.id.editar_correo);
        EditText phone = form.findViewById(R.id.editar_tel);
        TextView error = form.findViewById(R.id.editar_error);
        name.setText(value(perfil, "nombre"));
        mail.setText(value(perfil, "correo"));
        phone.setText(value(perfil, "telefono"));
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Editar mis datos").setView(form)
                .setNegativeButton("Cancelar", null).setPositiveButton("Guardar", null).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String n = name.getText().toString().trim();
            String m = mail.getText().toString().trim();
            String p = phone.getText().toString().trim();
            if (n.isEmpty() || m.isEmpty()) {
                error.setText("Nombre y correo son obligatorios."); return;
            }
            loading = true;
            dialog.setCancelable(false);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
            error.setText("Guardando…");
            String current = token;
            async(() -> Api.editar(current, n, m, p), result -> {
                loading = false;
                perfil = result;
                dialog.dismiss();
                showDashboard();
                Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show();
            }, e -> {
                loading = false;
                dialog.setCancelable(true);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
                if (e instanceof Api.ErrorHttp && ((Api.ErrorHttp)e).code == 401) {
                    dialog.dismiss(); expired(e);
                } else error.setText(message(e));
            });
        });
    }

    private void setText(View screen, int id, String text) {
        TextView view = screen.findViewById(id);
        view.setText(text);
    }
    private void setPill(View screen, int id, String label, boolean good) {
        TextView view = screen.findViewById(id);
        view.setText(label);
        view.setTextColor(good ? GREEN : AMBER);
        view.setBackgroundResource(good ? R.drawable.gt_good : R.drawable.gt_warn);
    }
    private String value(JSONObject o, String key) {
        return o == null || o.isNull(key) || !o.has(key) ? "" : o.optString(key, "");
    }
    private String display(JSONObject o, String key) {
        String s = value(o, key); return s.isEmpty() ? "No registrado" : s;
    }
    private String initials(String name) {
        if (name.trim().isEmpty()) return "S";
        String[] parts = name.trim().split("\\s+");
        String s = parts[0].substring(0, 1);
        if (parts.length > 1) s += parts[1].substring(0, 1);
        return s.toUpperCase(new Locale("es", "MX"));
    }
    private String date(String iso) {
        if (iso.isEmpty()) return "No registrada";
        try {
            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            in.setLenient(false);
            return new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "MX")).format(in.parse(iso));
        } catch (Exception e) { return iso; }
    }
    private String money(JSONObject o, String key) {
        try {
            return NumberFormat.getCurrencyInstance(new Locale("es", "MX"))
                    .format(new BigDecimal(value(o, key))) + " MXN";
        } catch (Exception e) { return "No disponible"; }
    }
    private void clearSession() {
        token = null; perfil = null; membresia = null; planes = null;
        prefs.edit().remove("token").apply(); tab = 0;
    }
    private void logout() {
        String old = token;
        clearSession();
        showLogin("");
        if (old != null) async(() -> { Api.logout(old); return true; }, x -> {}, e -> {});
    }
    @Override public void onBackPressed() {
        if (token != null && !loading && tab != 0 && perfil != null) {
            tab = 0; showDashboard();
        } else super.onBackPressed();
    }
}
