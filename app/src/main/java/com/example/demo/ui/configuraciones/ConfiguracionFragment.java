package com.example.demo.ui.configuraciones;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.databinding.FragmentConfiguracionBinding;
import com.example.demo.main.KeyDwonFragment;
import com.example.demo.tools.UIHelper;
import com.lidroid.xutils.ViewUtils;
import com.rscja.deviceapi.entity.Gen2Entity;
import com.rscja.deviceapi.entity.InventoryModeEntity;

import java.util.HashMap;
import java.util.Map;

public class ConfiguracionFragment extends KeyDwonFragment implements View.OnClickListener {

    private MainActivity mContext;

    private Button btnSetFre;
    private Button btnGetFre;
    private Spinner spMode, spPower;
    private LinearLayout ll_freHop;
    //    private EditText et_worktime, et_waittime;
    //    private Button btnWorkWait;
    private Spinner spFreHop; //Lista de frecuencia
    private Button btnSetFreHop; //Establecer ajustes de frecuencia
    private TextView tv_normal_set; //Configuración normal (haga clic 5 veces para configurar la configuración de frecuencia)
    //    private Button btnGetWait; //Obtener la proporción vacía
    private Button btnSetAgreement; //Establecer protocolo
    private Spinner SpinnerAgreement; //Lista de protocolos
    private Button btnSetLinkParams; //Establecer parámetros de enlace
    private Button btnGetLinkParams; //Obtener parámetros de enlace
    private Spinner splinkParams; //Lista de parámetros de enlace
    //    private Button btnSetQTParams; //Establecer parámetros QT
    //    private Button btnGetQTParams; //Obtener parámetros QT
    //    private CheckBox cbQt; //Abrir QT
    CheckBox cbTagFocus; //Abrir etiqueta Enfoque
    CheckBox cbFastID; //Abrir identificación rápida
    CheckBox cbEPC_TID; //Abrir EPC+TID
    RadioButton rb_America; //Frecuencia americana
    RadioButton rb_Others; //Otras frecuencias
    private ArrayAdapter adapter; //Adaptador de lista de frecuencias

    LinearLayout llUserPtr, llUserLen;
    EditText etUserPtr, etUserLen;
    RadioButton rbEPCTIDUSER, rbEPCTID, rbEPC, rbUnknown;
    Button btnSetInventory, btnGetInventory;
    Spinner spSessionID, spInventoried;
    Button btnSetSession, btnGetSession;
    String TAG = "UHFSetFragment";
    Button btnGetPower, btnSetPower;

    private DisplayMetrics metrics;
    private AlertDialog dialog;
    private long[] timeArr;

    private Handler mHandler = new Handler();
    private int arrPow; //输出功率

    private String[] arrayMode;

    private FragmentConfiguracionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ConfiguracionViewModel configuracionesViewModel =
                new ViewModelProvider(this).get(ConfiguracionViewModel.class);

        binding = FragmentConfiguracionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ViewUtils.inject(this, root);
        llUserPtr = root.findViewById(R.id.llUserPtr);
        llUserLen = root.findViewById(R.id.llUserLen);
        etUserPtr = root.findViewById(R.id.etUserPtr);
        etUserLen = root.findViewById(R.id.etUserLen);
        rbEPCTIDUSER = root.findViewById(R.id.rbEPCTIDUSER);
        rbEPCTID = root.findViewById(R.id.rbEPCTID);
        rbEPC = root.findViewById(R.id.rbEPC);
        rbUnknown = root.findViewById(R.id.rbUnknown);

        btnSetInventory = root.findViewById(R.id.btnSetInventory);
        btnGetInventory = root.findViewById(R.id.btnGetInventory);

        spSessionID = root.findViewById(R.id.spSessionID);
        spInventoried = root.findViewById(R.id.spInventoried);
        btnGetSession = root.findViewById(R.id.btnGetSession);
        btnSetSession = root.findViewById(R.id.btnSetSession);

        btnGetPower = root.findViewById(R.id.btnGetPower1);
        btnSetPower = root.findViewById(R.id.btnSetPower1);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Inicilizar las variables
        ll_freHop = (LinearLayout) getView().findViewById(R.id.ll_freHop);
        spPower = (Spinner) getView().findViewById(R.id.spPower);
        spFreHop = (Spinner) getView().findViewById(R.id.spFreHop);
        btnSetFreHop = (Button) getView().findViewById(R.id.btnSetFreHop);
        tv_normal_set = (TextView) getView().findViewById(R.id.tv_normal_set);
        btnSetAgreement = (Button) getView().findViewById(R.id.btnSetAgreement);
        SpinnerAgreement = (Spinner) getView().findViewById(R.id.SpinnerAgreement);
        btnGetLinkParams = (Button) getView().findViewById(R.id.btnGetLinkParams);
        btnSetLinkParams = (Button) getView().findViewById(R.id.btnSetLinkParams);
        splinkParams = (Spinner) getView().findViewById(R.id.splinkParams);
        cbTagFocus = (CheckBox) getView().findViewById(R.id.cbTagFocus);
        cbFastID = (CheckBox) getView().findViewById(R.id.cbFastID);
        cbEPC_TID = (CheckBox) getView().findViewById(R.id.cbEPC_TID);
        rb_America = (RadioButton) getView().findViewById(R.id.rb_America);
        rb_Others = (RadioButton) getView().findViewById(R.id.rb_Others);



        mContext = (MainActivity) getActivity();
        arrayMode = getResources().getStringArray(R.array.matrisfre);

        btnSetFre = (Button) getView().findViewById(R.id.BtSetFre);
        btnGetFre = (Button) getView().findViewById(R.id.BtGetFre);

        spMode = (Spinner) getView().findViewById(R.id.SpinnerMode);
        spMode.setOnItemSelectedListener(new MyOnTouchListener());

        btnSetFre.setOnClickListener(new SetFreOnclickListener());
        btnGetFre.setOnClickListener(new GetFreOnclickListener());
//        btnWorkWait.setOnClickListener(new SetPWMOnclickListener());
//        btnGetWait.setOnClickListener(this);

        btnSetFreHop.setOnClickListener(this);
        tv_normal_set.setOnClickListener(this);
        btnSetAgreement.setOnClickListener(this);
//        btnSetQTParams.setOnClickListener(this);
//        btnGetQTParams.setOnClickListener(this);
        btnSetLinkParams.setOnClickListener(this);
        btnGetLinkParams.setOnClickListener(this);

        rbEPCTIDUSER.setOnClickListener(this);
        rbEPCTID.setOnClickListener(this);
        rbEPC.setOnClickListener(this);

        btnSetInventory.setOnClickListener(this);
        btnGetInventory.setOnClickListener(this);

        btnGetSession.setOnClickListener(this);
        btnSetSession.setOnClickListener(this);

        btnGetPower.setOnClickListener(this);
        btnSetPower.setOnClickListener(this);

        cbTagFocus.setOnCheckedChangeListener(new OnMyCheckedChangedListener());
        cbFastID.setOnCheckedChangeListener(new OnMyCheckedChangedListener());
        cbEPC_TID.setOnCheckedChangeListener(new OnMyCheckedChangedListener());
        cbEPC_TID.setVisibility(View.GONE);
        String ver = mContext.mReader.getVersion();
        arrPow = R.array.arrayPower;
        ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext, arrPow, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPower.setAdapter(adapter);

        // Establece un OnClickListener en la vista RadioButton
        rb_America.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tu lógica de clic aquí
                final ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext, R.array.arrayFreHop_us, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFreHop.setAdapter(adapter);
            }
        });

        // Establece un OnClickListener en la vista RadioButton
        rb_Others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tu lógica de clic aquí
                final ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext, R.array.arrayFreHop, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spFreHop.setAdapter(adapter);
            }
        });



    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            /*
            开启子线程获取参数，Handler更新UI,防止fragment打开卡顿
             */
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    getFre();
//                    getPwm();
                    getLinkParams();
                    OnClick_GetPower(null);
                    getEpcTidUserMode(false);
                    getSession();
                }
            });
        }
    }

    /**
     * 工作模式下拉列表点击选中item监听
     */
    public class MyOnTouchListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (spMode.getSelectedItem().toString().equals(getString(R.string.United_States_Standard))) {
                ll_freHop.setVisibility(View.VISIBLE);
                rb_America.setChecked(true); //默认美国频点
            } else if (position != 3) {
                ll_freHop.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class SetFreOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            String strMode = spMode.getSelectedItem().toString();
            int mode = getMode(strMode);
            Log.d(TAG, "setFrequencyMode mode=" + mode);
            if (mContext.mReader.setFrequencyMode((byte) mode)) {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_frequency_succ);
            } else {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_frequency_fail);
            }
        }
    }

    public void getFre() {
        int mode = mContext.mReader.getFrequencyMode();
        Log.e(TAG, "getFrequencyMode()=" + mode);
        if (mode != -1) {
            int count = spMode.getCount();
            int idx = getModeIndex(mode);
            //Log.e("TAG", "spMode  " + getResources().getStringArray(R.array.arrayMode).length + "  " + (idx > count - 1 ? count - 1 : idx));
            spMode.setSelection(idx > count - 1 ? count - 1 : idx);
        } else {
            UIHelper.ToastMessage(mContext, R.string.uhf_msg_read_frequency_fail);
        }
    }

    public void getLinkParams() {
        int idx = mContext.mReader.getRFLink();
        Log.e(TAG, "getLinkParams()=" + idx);
        if (idx != -1) {
            splinkParams.setSelection(idx);

//			UIHelper.ToastMessage(mContext,
//					R.string.uhf_msg_get_para_succ);
        } else {
            UIHelper.ToastMessage(mContext,
                    R.string.uhf_msg_set_fail);
        }
    }

    private int getMode(String modeName) {
        if (modeName.equals(getString(R.string.China_Standard_840_845MHz))) {
            return 0x01;
        } else if (modeName.equals(getString(R.string.China_Standard_920_925MHz))) {
            return 0x02;
        } else if (modeName.equals(getString(R.string.ETSI_Standard))) {
            return 0x04;
        } else if (modeName.equals(getString(R.string.United_States_Standard))) {
            return 0x08;
        } else if (modeName.equals(getString(R.string.Korea))) {
            return 0x16;
        } else if (modeName.equals(getString(R.string.Japan))) {
            return 0x32;
        } else if (modeName.equals(getString(R.string.South_Africa_915_919MHz))) {
            return 0x33;
        } else if (modeName.equals(getString(R.string.New_Zealand))) {
            return 0x34;
        } else if (modeName.equals(getString(R.string.Morocco))) {
            return 0x80;
        }
        return 0x08;
    }

    private String getModeName(int mode) {
        switch (mode) {
            case 0x01:
                return getString(R.string.China_Standard_840_845MHz);
            case 0x02:
                return getString(R.string.China_Standard_920_925MHz);
            case 0x04:
                return getString(R.string.ETSI_Standard);
            case 0x08:
                return getString(R.string.United_States_Standard);
            case 0x16:
                return getString(R.string.Korea);
            case 0x32:
                return getString(R.string.Japan);
            case 0x33:
                return getString(R.string.South_Africa_915_919MHz);
            case 0x34:
                return getString(R.string.New_Zealand);
            case 0x80:
                return getString(R.string.Morocco);
            default:
                return getString(R.string.United_States_Standard);
        }
    }


    private int getModeIndex(String modeName) {
        for (int i = 0; i < arrayMode.length; i++) {
            if (arrayMode[i].equals(modeName)) {
                return i;
            }
        }
        return 0;
    }

    private int getModeIndex(int mode) {
        return getModeIndex(getModeName(mode));
    }

    public class GetFreOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            getFre();
        }
    }

    public class OnMyCheckedChangedListener implements CompoundButton.OnCheckedChangeListener {

        // Mapa que relaciona los IDs de las vistas con las acciones correspondientes
        private Map<Integer, Runnable> actionsMap = new HashMap<>();

        // Constructor donde inicializamos el mapa con las acciones correspondientes a cada ID de vista
        public OnMyCheckedChangedListener() {
            actionsMap.put(R.id.cbTagFocus, new Runnable() {
                @Override
                public void run() {
                    boolean isChecked = cbTagFocus.isChecked();
                    if (mContext.mReader.setTagFocus(isChecked)) {
                        cbTagFocus.setText(isChecked ? R.string.tagFocus_off : R.string.tagFocus);
                        UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
                    } else {
                        UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
                    }
                }
            });

            actionsMap.put(R.id.cbFastID, new Runnable() {
                @Override
                public void run() {
                    boolean isChecked = cbFastID.isChecked();
                    if (mContext.mReader.setFastID(isChecked)) {
                        cbFastID.setText(isChecked ? R.string.fastID_off : R.string.fastID);
                        UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
                    } else {
                        UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
                    }
                }
            });

            actionsMap.put(R.id.cbEPC_TID, new Runnable() {
                @Override
                public void run() {
                    boolean isChecked = cbEPC_TID.isChecked();
                    if (isChecked) {
                        cbEPC_TID.setText(R.string.EPC_TID_off);
                        if (mContext.mReader.setEPCAndTIDMode()) {
                            UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
                        } else {
                            UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
                        }
                    } else {
                        cbEPC_TID.setText(R.string.EPC_TID);
                        if (mContext.mReader.setEPCMode()) {
                            UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
                        } else {
                            UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
                        }
                    }
                }
            });
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // Obtén el ID de la vista que cambió su estado
            int viewId = buttonView.getId();

            // Busca la acción correspondiente al ID de la vista y ejecútala
            Runnable action = actionsMap.get(viewId);
            if (action != null) {
                action.run();
            }
        }
    }

    public void OnClick_GetPower(View view) {
        int iPower = mContext.mReader.getPower();

        Log.i("UHFSetFragment", "OnClick_GetPower() iPower=" + iPower);

        if (iPower > -1) {
            int position = iPower - 1;
            int count = spPower.getCount();
            spPower.setSelection(position > count - 1 ? count - 1 : position);

            // UIHelper.ToastMessage(mContext,
            // R.string.uhf_msg_read_power_succ);

        } else {
            UIHelper.ToastMessage(mContext, R.string.uhf_msg_read_power_fail);
        }

    }

    public void OnClick_SetPower(View view) {
        int iPower = spPower.getSelectedItemPosition() + 1;

        Log.i("UHFSetFragment", "OnClick_SetPower() iPower=" + iPower);

        if (mContext.mReader.setPower(iPower)) {

            UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_power_succ);
        } else {
            UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_power_fail);
//            mContext.playSound(2);
        }

    }

    private boolean setFreHop(float value) {
        boolean result = mContext.mReader.setFreHop(value);
        if (result) {

            UIHelper.ToastMessage(mContext,
                    R.string.uhf_msg_set_frehop_succ);
        } else {
            UIHelper.ToastMessage(mContext,
                    R.string.uhf_msg_set_frehop_fail);
//            mContext.playSound(2);
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        // Crea un mapa que asocie los IDs de las vistas con las acciones correspondientes
        Map<Integer, Runnable> actionsMap = new HashMap<>();

        // Agrega las acciones correspondientes a cada ID de vista al mapa
        actionsMap.put(R.id.btnSetFreHop, new Runnable() {
            @Override
            public void run() {
                View view = spFreHop.getSelectedView();
                if (view instanceof TextView) {
                    String freHop = ((TextView) view).getText().toString().trim();
                    setFreHop(Float.valueOf(freHop)); // Establecer punto de frecuencia
                }
            }
        });

        actionsMap.put(R.id.btnSetAgreement, new Runnable() {
            @Override
            public void run() {
                if (mContext.mReader.setProtocol(SpinnerAgreement.getSelectedItemPosition())) {
                    UIHelper.ToastMessage(mContext, R.string.setAgreement_succ);
                } else {
                    UIHelper.ToastMessage(mContext, R.string.setAgreement_fail);
                }
            }
        });

        actionsMap.put(R.id.btnSetLinkParams, new Runnable() {
            @Override
            public void run() {
                if (mContext.mReader.setRFLink(splinkParams.getSelectedItemPosition())) {
                    UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
                } else {
                    UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
                    //mContext.playSound(2);
                }
            }
        });

        actionsMap.put(R.id.btnGetLinkParams, new Runnable() {
            @Override
            public void run() {
                getLinkParams();
            }
        });

        actionsMap.put(R.id.rbEPCTIDUSER, new Runnable() {
            @Override
            public void run() {
                llUserLen.setVisibility(View.VISIBLE);
                llUserPtr.setVisibility(View.VISIBLE);
            }
        });

        actionsMap.put(R.id.rbEPCTID, new Runnable() {
            @Override
            public void run() {
                llUserLen.setVisibility(View.GONE);
                llUserPtr.setVisibility(View.GONE);
            }
        });

        actionsMap.put(R.id.rbEPC, new Runnable() {
            @Override
            public void run() {
                llUserLen.setVisibility(View.GONE);
                llUserPtr.setVisibility(View.GONE);
            }
        });

        actionsMap.put(R.id.btnSetInventory, new Runnable() {
            @Override
            public void run() {
                setEpcTidUserMode();
            }
        });

        actionsMap.put(R.id.btnGetInventory, new Runnable() {
            @Override
            public void run() {
                getEpcTidUserMode(true);
            }
        });

        actionsMap.put(R.id.btnGetSession, new Runnable() {
            @Override
            public void run() {
                Log.e("getSession", String.valueOf(getSession()));
                if (getSession()) {
                    UIHelper.ToastMessage(mContext, R.string.uhf_msg_get_para_succ);
                } else {
                    UIHelper.ToastMessage(mContext, R.string.uhf_msg_get_para_fail);
                }
            }
        });

        actionsMap.put(R.id.btnSetSession, new Runnable() {
            @Override
            public void run() {
                setSession();
            }
        });

        actionsMap.put(R.id.btnGetPower1, new Runnable() {
            @Override
            public void run() {
                OnClick_GetPower(null);
            }
        });

        actionsMap.put(R.id.btnSetPower1, new Runnable() {
            @Override
            public void run() {
                OnClick_SetPower(null);
            }
        });


        // Obtiene la acción correspondiente al ID de la vista que se hizo clic y la ejecuta
        Runnable action = actionsMap.get(v.getId());
        if (action != null) {
            action.run();
        }

    }

    private boolean getSession() {
        Gen2Entity p = mContext.mReader.getGen2();
        if (p != null) {
            spSessionID.setSelection(p.getQuerySession());
            spInventoried.setSelection(p.getQueryTarget());
            return true;
        }
        return false;
    }

    private void setSession() {
        int seesionid = spSessionID.getSelectedItemPosition();
        int inventoried = spInventoried.getSelectedItemPosition();
        if (seesionid < 0 || inventoried < 0) {
            return;
        }
        Gen2Entity p = mContext.mReader.getGen2();
        if (p != null) {
            p.setQueryTarget(inventoried);
            p.setQuerySession(seesionid);
            if (mContext.mReader.setGen2(p)) {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
            } else {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
            }
        } else {
            UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
        }
    }


    private void showFrequencyDialog() {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//	        builder.setTitle(R.string.btSetFrequency);
            View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_configuracion, null);
            ListView listView = (ListView) view.findViewById(R.id.listView_frequency);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_dismissDialog);
            iv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });

            String[] strArr = getResources().getStringArray(R.array.arrayFreHop);
            listView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_text1, strArr));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    if (view instanceof TextView) {
                        TextView tv = (TextView) view;
                        float value = Float.valueOf(tv.getText().toString().trim());
                        setFreHop(value); //设置频点
                        dialog.dismiss();
                    }
                }

            });

            builder.setView(view);
            dialog = builder.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = getWindowWidth() - 100;
            params.height = getWindowHeight() - 200;
            dialog.getWindow().setAttributes(params);
        } else {
            dialog.show();
        }
    }

    private boolean isFiveClick() {
        if (timeArr == null) {
            timeArr = new long[5];
        }
        System.arraycopy(timeArr, 1, timeArr, 0, timeArr.length - 1);
        timeArr[timeArr.length - 1] = System.currentTimeMillis();
        return System.currentTimeMillis() - timeArr[0] < 1600;
    }


    public int getWindowWidth() {
        if (metrics == null) {
            metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }
        return metrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public int getWindowHeight() {
        if (metrics == null) {
            metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }
        return metrics.heightPixels;
    }



    private void getEpcTidUserMode(boolean isToast) {
        llUserPtr.setVisibility(View.GONE);
        llUserLen.setVisibility(View.GONE);
        InventoryModeEntity inventoryModeEntity = mContext.mReader.getEPCAndTIDUserMode();
        if (inventoryModeEntity != null) {
            int mode = inventoryModeEntity.getMode();
            if (mode == 0) {
                //epc
                rbEPC.setChecked(true);
                Log.i("getEpcTidUserMode", "getEpcTidUserMode = 0");
            } else if (mode == 1) {
                //epc+TID
                rbEPCTID.setChecked(true);
                Log.i("getEpcTidUserMode", "getEpcTidUserMode = 1");
            } else if (mode == 2) {
                //epc+TID+user
                rbEPCTIDUSER.setChecked(true);
                llUserPtr.setVisibility(View.VISIBLE);
                llUserLen.setVisibility(View.VISIBLE);
                etUserPtr.setText(inventoryModeEntity.getUserOffset() + "");
                etUserLen.setText(inventoryModeEntity.getUserLength() + "");
                Log.i("getEpcTidUserMode", "getEpcTidUserMode = 2");
            }
        } else {
            if (isToast) {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
            }
        }
    }

    private void setEpcTidUserMode() {

        if (rbEPC.isChecked()) {
            if (mContext.mReader.setEPCMode()) {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
            } else {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
            }
        } else if (rbEPCTID.isChecked()) {
            if (mContext.mReader.setEPCAndTIDMode()) {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
            } else {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
            }
        } else if (rbEPCTIDUSER.isChecked()) {
            String strUserPtr = etUserPtr.getText().toString();
            String strUserLen = etUserLen.getText().toString();
            int userPtr = 0;
            int userLen = 6;
            if (!TextUtils.isEmpty(strUserPtr)) {
                userPtr = Integer.parseInt(strUserPtr);
            }
            if (!TextUtils.isEmpty(strUserLen)) {
                userLen = Integer.parseInt(strUserLen);
            }

            if (mContext.mReader.setEPCAndTIDUserMode(userPtr, userLen)) {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
            } else {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
            }
        }

    }



}