package com.example.demo;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.demo.main.BaseTabFragmentActivity;
import com.example.demo.tools.UhfInfo;
import com.example.demo.ui.Inventario.InventarioViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseTabFragmentActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private final static String TAG = "MainActivity";
    public UhfInfo uhfInfo = new UhfInfo();
    public ArrayList<HashMap<String, String>> tagList = new ArrayList<>();
    public boolean loopFlag = false;
    private static final int TARGET_VERSION = 22;
    private static final int PERMISSIONS_REQUEST = 1;
    private static String[] PERMISSIONS_LIST =
            {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
    private InventarioViewModel inventarioViewModel;

    private ActivityResultLauncher<Intent> manageAllFilesAccessPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (!Environment.isExternalStorageManager()) {
                            Toast.makeText(MainActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    private ActivityResultLauncher<String[]> requestPermissionsLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    boolean allPermissionsGranted = true;
                    for (Boolean granted : result.values()) {
                        allPermissionsGranted &= granted;
                    }
                    if (!allPermissionsGranted) {
                        Toast.makeText(MainActivity.this, "Permissions required for Bluetooth functionality", Toast.LENGTH_SHORT).show();
                    } else {
                        launchBluetoothActivity();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkReadWritePermission();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Mostrar la barra de navegación
        setSupportActionBar(binding.appBarMain.toolbar);

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).setAnchorView(R.id.fab).show();
            }
        });
        // Initialize the drawer and navigation view
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Setup AppBarConfiguration
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_proveedores, R.id.nav_clientes,
                R.id.nav_inventario, R.id.nav_configuraciones, R.id.nav_salidas, R.id.nav_entradas)
                .setOpenableLayout(drawer)
                .build();

        // Find the NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        // Setup ActionBar with NavController and AppBarConfiguration
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // Setup NavigationView with NavController
        NavigationUI.setupWithNavController(navigationView, navController);
        // Inicializar el ViewModel
        inventarioViewModel = new ViewModelProvider(this).get(InventarioViewModel.class);

        // Initialize UHF and sound
        initUHF();
        initSound();
        // Llamar al método para limpiar y reiniciar la aplicación
        limpiarYReiniciarApp();
        // PopBackStack hasta el destino nav_home
        navController.popBackStack(R.id.nav_home, false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void checkReadWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                manageAllFilesAccessPermissionLauncher.launch(intent);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionsLauncher.launch(PERMISSIONS_LIST);
        } else {
            launchBluetoothActivity();
        }
    }

    private void launchBluetoothActivity() {
        // Código para lanzar la actividad Bluetooth
    }

    // Código para inicializar sonido y demás funcionalidades
    private HashMap<Integer, Integer> soundMap = new HashMap<>();
    private SoundPool soundPool;
    private float volumnRatio;
    private AudioManager am;

    private void initSound() {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        soundMap.put(1, soundPool.load(this, R.raw.barcodebeep, 1));
        soundMap.put(2, soundPool.load(this, R.raw.serror, 1));
        am = (AudioManager) this.getSystemService(AUDIO_SERVICE);
    }

    private void releaseSoundPool() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    public void playSound(int id) {
        float audioMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float audioCurrentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumnRatio = audioCurrentVolume / audioMaxVolume;
        try {
            soundPool.play(soundMap.get(id), volumnRatio, volumnRatio, 1, 0, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }


    private void limpiarYReiniciarApp() {
        // Limpia preferencias compartidas
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().clear().apply();
        // Llamar al método de limpiar datos en el ViewModel
        inventarioViewModel.limpiarDatos();
    }
}