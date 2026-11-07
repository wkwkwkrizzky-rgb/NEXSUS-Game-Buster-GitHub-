package com.nexus.gamebuster;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity {
    private final int BG = Color.rgb(10, 6, 18);
    private final int PANEL = Color.rgb(28, 21, 39);
    private final int PURPLE = Color.rgb(174, 105, 255);
    private LinearLayout content;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(23, 16, 32));
        getWindow().setNavigationBarColor(BG);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setBackgroundColor(BG);

        TextView top = new TextView(this);
        top.setText("NEXSUS  GAME BUSTER");
        top.setTextColor(Color.WHITE);
        top.setTextSize(20);
        top.setTypeface(null, Typeface.BOLD);
        top.setPadding(dp(20), dp(22), dp(12), dp(20));
        top.setBackgroundColor(Color.rgb(23,16,32));
        page.addView(top);

        HorizontalScrollView navScroll = new HorizontalScrollView(this);
        navScroll.setHorizontalScrollBarEnabled(false);
        LinearLayout nav = new LinearLayout(this);
        nav.setPadding(dp(8), dp(8), dp(8), dp(8));
        String[] tabs = {"BERANDA", "GAME", "SENSITIVITY", "GRAFIK", "RAM"};
        for (String t : tabs) {
            Button b = new Button(this);
            b.setText(t);
            b.setTextSize(11);
            b.setTextColor(Color.WHITE);
            b.setAllCaps(false);
            b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(PANEL));
            b.setOnClickListener(v -> showTab(t));
            nav.addView(b, new LinearLayout.LayoutParams(-2, dp(48)));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)b.getLayoutParams();
            lp.setMargins(dp(4), 0, dp(4), 0);
            b.setLayoutParams(lp);
        }
        navScroll.addView(nav);
        page.addView(navScroll);

        ScrollView scroll = new ScrollView(this);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(16), dp(18), dp(24));
        scroll.addView(content);
        page.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(page);
        showTab("BERANDA");
    }

    private void showTab(String tab) {
        content.removeAllViews();
        if (tab.equals("BERANDA")) {
            title("Selamat datang di NEXSUS");
            subtitle("Pusat pintasan dan panduan pengaturan game.");
            card("GAME BUSTER", "Lihat game yang terpasang dan buka game langsung dari sini.");
            action("Tampilkan game saya", () -> showTab("GAME"));
            card("SENSITIVITY", "Pilih preset sebagai panduan. Aplikasi ini tidak mengubah sensitivitas di dalam game secara otomatis.");
            action("Atur preset sensitivitas", () -> showTab("SENSITIVITY"));
            card("GRAFIK", "Pilih rekomendasi grafis sesuai kemampuan perangkat.");
            action("Lihat pilihan grafik", () -> showTab("GRAFIK"));
            card("PERFORMANCE", "Lihat informasi RAM perangkat. Android mengelola memori secara otomatis.");
            action("Lihat informasi RAM", () -> showTab("RAM"));
        } else if (tab.equals("GAME")) {
            title("GAME BUSTER");
            subtitle("Daftar aplikasi yang memiliki ikon peluncur. Ketuk BUKA untuk menjalankannya.");
            showInstalledGames();
        } else if (tab.equals("SENSITIVITY")) {
            title("SENSITIVITY PRESETS");
            subtitle("Preset berikut hanya catatan panduan. Sesuaikan lagi melalui menu kontrol di game yang kamu mainkan.");
            preset("STABIL", "Mulai dari sensitivitas sedang. Cocok untuk mencoba kontrol yang lebih konsisten.");
            preset("SEIMBANG", "Gunakan nilai sedang–tinggi jika gerakan terasa terlalu lambat.");
            preset("RESPONSIF", "Coba nilai lebih tinggi secara bertahap. Turunkan kembali jika bidikan terasa sulit dikendalikan.");
            note("Catatan: tidak ada satu angka sensitivitas yang cocok untuk semua perangkat, game, atau pemain.");
        } else if (tab.equals("GRAFIK")) {
            title("PILIHAN GRAFIK");
            subtitle("Ini adalah rekomendasi. Pengaturan grafis sebenarnya perlu diubah dari menu game.");
            preset("LOW GRAPHICS", "Utamakan kelancaran dan suhu perangkat yang lebih terkendali.");
            preset("BALANCED GRAPHICS", "Seimbangkan kualitas visual dan kelancaran permainan.");
            preset("ULTRA GRAPHICS", "Gunakan hanya jika game dan perangkat mendukungnya dengan stabil.");
            note("NEXSUS tidak memaksa resolusi, FPS, atau kualitas grafis aplikasi lain.");
        } else if (tab.equals("RAM")) {
            title("PERFORMANCE / RAM");
            subtitle("Informasi memori perangkat saat ini. Angka dapat berubah ketika aplikasi lain berjalan.");
            showRam();
            action("Perbarui informasi RAM", () -> showTab("RAM"));
            note("Android mengatur RAM secara otomatis. NEXSUS tidak mematikan proses sistem atau mengklaim dapat menambah RAM.");
        }
    }

    private void showInstalledGames() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = getPackageManager().queryIntentActivities(intent, 0);
        Collections.sort(apps, new Comparator<ResolveInfo>() {
            public int compare(ResolveInfo a, ResolveInfo b) {
                return String.valueOf(a.loadLabel(getPackageManager())).compareToIgnoreCase(
                    String.valueOf(b.loadLabel(getPackageManager())));
            }
        });
        if (apps.isEmpty()) { note("Tidak ditemukan aplikasi yang bisa dibuka."); return; }
        for (ResolveInfo info : apps) {
            String label = String.valueOf(info.loadLabel(getPackageManager()));
            String pkg = info.activityInfo.packageName;
            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(dp(12), dp(8), dp(8), dp(8));
            row.setBackgroundColor(PANEL);
            ImageView icon = new ImageView(this);
            icon.setImageDrawable(info.loadIcon(getPackageManager()));
            row.addView(icon, new LinearLayout.LayoutParams(dp(42), dp(42)));
            TextView name = new TextView(this);
            name.setText(label);
            name.setTextColor(Color.WHITE);
            name.setTextSize(14);
            name.setPadding(dp(12), 0, dp(6), 0);
            row.addView(name, new LinearLayout.LayoutParams(0, -2, 1));
            Button open = new Button(this);
            open.setText("BUKA");
            open.setTextSize(11);
            open.setAllCaps(false);
            open.setTextColor(Color.WHITE);
            open.setBackgroundTintList(android.content.res.ColorStateList.valueOf(PURPLE));
            open.setOnClickListener(v -> {
                Intent launch = getPackageManager().getLaunchIntentForPackage(pkg);
                if (launch != null) startActivity(launch);
                else Toast.makeText(this, "Aplikasi tidak dapat dibuka.", Toast.LENGTH_SHORT).show();
            });
            row.addView(open);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
            lp.setMargins(0, 0, 0, dp(8));
            content.addView(row, lp);
        }
    }

    private void showRam() {
        ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        long total = mi.totalMem / (1024 * 1024);
        long available = mi.availMem / (1024 * 1024);
        long used = total - available;
        card("RAM TERPAKAI (PERKIRAAN)", used + " MB");
        card("RAM TERSEDIA", available + " MB");
        card("TOTAL RAM TERDETEKSI", total + " MB");
        card("STATUS", mi.lowMemory ? "Memori sedang rendah" : "Tidak terdeteksi kondisi memori rendah");
    }

    private void title(String s) {
        TextView v = new TextView(this); v.setText(s); v.setTextColor(Color.WHITE);
        v.setTextSize(24); v.setTypeface(null, Typeface.BOLD); v.setPadding(0, dp(8), 0, dp(8));
        content.addView(v);
    }
    private void subtitle(String s) {
        TextView v = new TextView(this); v.setText(s); v.setTextColor(PURPLE);
        v.setTextSize(14); v.setPadding(0, 0, 0, dp(18)); content.addView(v);
    }
    private void card(String h, String body) {
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(16), dp(14), dp(16), dp(14)); box.setBackgroundColor(PANEL);
        TextView head = new TextView(this); head.setText(h); head.setTextColor(PURPLE);
        head.setTextSize(13); head.setTypeface(null, Typeface.BOLD);
        TextView text = new TextView(this); text.setText(body); text.setTextColor(Color.WHITE);
        text.setTextSize(15); text.setPadding(0, dp(8), 0, 0);
        box.addView(head); box.addView(text);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 0, 0, dp(10)); content.addView(box, lp);
    }
    private void preset(String h, String body) { card(h, body); }
    private void note(String s) {
        TextView v = new TextView(this); v.setText(s); v.setTextColor(Color.LTGRAY);
        v.setTextSize(13); v.setPadding(dp(4), dp(8), dp(4), dp(14)); content.addView(v);
    }
    private void action(String label, Runnable r) {
        Button b = new Button(this); b.setText(label); b.setAllCaps(false); b.setTextColor(Color.WHITE);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(PURPLE));
        b.setOnClickListener(v -> r.run());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, dp(52));
        lp.setMargins(0, 0, 0, dp(14)); content.addView(b, lp);
    }
    private int dp(int v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }
}
