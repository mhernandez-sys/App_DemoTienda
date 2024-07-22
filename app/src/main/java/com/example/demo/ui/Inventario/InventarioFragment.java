package com.example.demo.ui.Inventario;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.demo.MainActivity;
import com.example.demo.R;
import com.example.demo.WebServiceManager;
import com.example.demo.databinding.FragmentInicioBinding;
import com.example.demo.databinding.FragmentInventarioBinding;
import com.example.demo.main.KeyDwonFragment;
import com.example.demo.tools.NumberTool;
import com.example.demo.tools.StringUtils;
import com.example.demo.tools.UIHelper;
import com.example.demo.tools.UhfInfo;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.interfaces.IUHFInventoryCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventarioFragment extends KeyDwonFragment {

    private FragmentInventarioBinding binding;
    private static final String TAG = "Lector UHF";
    //private boolean loopFlag = false;
    public int inventoryFlag = 1;
    public static List<String> tempDatas = new ArrayList<>();
    MyAdapter adapter;
    Button BtClear, BtInventory, BtVerificar;
    TextView tvTime;
    TextView tv_count;
    TextView tv_total;
    RadioGroup RgInventory;
    RadioButton RbInventorySingle;
    RadioButton RbInventoryLoop;

    public static ListView LvTags;
    public MainActivity mContext;
    public static HashMap<String, String> map;
    public static ArrayList<String> epcTidUser = new ArrayList<>();

    private int total;
    private long time;
    private WebServiceManager webServiceManager;


    private CheckBox cbFilter;
    private ViewGroup layout_filter;

    public static final String TAG_EPC = "tagEPC";
    public static final String TAG_EPC_TID = "tagEpcTID";
    public static final String TAG_COUNT = "tagCount";
    public static final String TAG_RSSI = "tagRssi";
    public String EPC ="";

    private CheckBox cbEPC_Tam;

    private Button btnSetFilter;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                UHFTAGInfo info = (UHFTAGInfo) msg.obj;
                Log.i(TAG, "UHFReadTagFragment.info="+info);
                String tid = info.getTid();
                String epc = info.getEPC();
                String user=info.getUser();
                Log.i(TAG, "UHFReadTagFragment.tid="+tid+" epc="+epc +" user="+user);
                addDataToList(epc,mergeTidEpc(tid, epc,user), info.getRssi());
            }else if (msg.what==2){
                if(mContext.loopFlag){
                    handler.sendEmptyMessageDelayed(2,10);
                    setTotalTime();
                }else {
                    handler.removeMessages(2);
                }
            }
        }
    };



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInventarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "Lector EPCs.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();
        mContext.currentFragment = this;
        webServiceManager = new WebServiceManager(getContext());

        //Botones del fragmenta
        BtClear = (Button) getView().findViewById(R.id.BtClear);
        BtInventory = (Button) getView().findViewById(R.id.BtInventory);
        BtVerificar = (Button) getView().findViewById(R.id.BtVerificar);
        //TextView del fragment
        tvTime = (TextView) getView().findViewById(R.id.tvTime);
        tvTime.setText("0s");
        tv_count = (TextView) getView().findViewById(R.id.tv_count);
        tv_total = (TextView) getView().findViewById(R.id.tv_total);
        RgInventory = (RadioGroup) getView().findViewById(R.id.RgInventory);
        RbInventorySingle = (RadioButton) getView().findViewById(R.id.RbInventorySingle);
        RbInventoryLoop = (RadioButton) getView().findViewById(R.id.RbInventoryLoop);

        LvTags = (ListView) getView().findViewById(R.id.LvTags);
        adapter = new MyAdapter(mContext);
        LvTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectItem(position);
                adapter.notifyDataSetInvalidated();
            }
        });

        LvTags.setAdapter(adapter);
        BtClear.setOnClickListener(new BtClearClickListener());
        BtVerificar.setOnClickListener(new BtInventarioClickListener());
        RgInventory.setOnCheckedChangeListener(new RgInventoryCheckedListener());
        BtInventory.setOnClickListener(new BtInventoryClickListener());
        initFilter(getView());
        initEPCTamperAlarm(getView());
        tv_count.setText(mContext.tagList.size()+"");
        tv_total.setText(total+"");
        Log.i(TAG, "Lector_C5.EtCountOfTags=" + tv_count.getText());


    }
    @Override
    public void onResume() {
        super.onResume();
        clearData();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(mContext);
        }

        public int getCount() {
            ///TODO AUTO-generated method stub
            return mContext.tagList.size();
        }

        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return mContext.tagList.get(arg0);
        }

        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.listtag_items, null);
                holder.tvEPCTID = (TextView) convertView.findViewById(R.id.TvTagUii);
                holder.tvTagCount = (TextView) convertView.findViewById(R.id.TvTagCount);
                holder.tvTagRssi = (TextView) convertView.findViewById(R.id.TvTagRssi);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvEPCTID.setText((String) mContext.tagList.get(position).get(TAG_EPC_TID));
            holder.tvTagCount.setText((String) mContext.tagList.get(position).get(TAG_COUNT));
            holder.tvTagRssi.setText((String) mContext.tagList.get(position).get(TAG_RSSI));

            if (position == selectItem) {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.azulc));
            } else {
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }
            return convertView;
        }

        public void setSelectItem(int select) {
            if (selectItem == select) {
                selectItem = -1;
                mContext.uhfInfo.setSelectItem("");
                mContext.uhfInfo.setSelectIndex(selectItem);
            } else {
                selectItem = select;
                mContext.uhfInfo.setSelectItem(mContext.tagList.get(select).get(TAG_EPC));
                mContext.uhfInfo.setSelectIndex(selectItem);
            }

        }

    }


    private void initFilter (View view){
        layout_filter = (ViewGroup) view.findViewById(R.id.layout_filter);
        layout_filter.setVisibility(View.GONE);
        cbFilter = (CheckBox) view.findViewById(R.id.cbFilter);
        cbFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                layout_filter.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        final EditText etLen = (EditText) view.findViewById(R.id.etLen);
        final EditText etPtr = (EditText) view.findViewById(R.id.etPtr);
        final EditText etData = (EditText) view.findViewById(R.id.etData);
        final RadioButton rbEPC = (RadioButton) view.findViewById(R.id.rbEPC);
        final RadioButton rbTID = (RadioButton) view.findViewById(R.id.rbTID);
        final RadioButton rbUser = (RadioButton) view.findViewById(R.id.rbUser);
        btnSetFilter = (Button) view.findViewById(R.id.btSet);

        btnSetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int filterBank = RFIDWithUHFUART.Bank_EPC;
                if (rbEPC.isChecked()) {
                    filterBank = RFIDWithUHFUART.Bank_EPC;
                } else if (rbTID.isChecked()) {
                    filterBank = RFIDWithUHFUART.Bank_TID;
                } else if (rbUser.isChecked()) {
                    filterBank = RFIDWithUHFUART.Bank_USER;
                }
                if (etLen.getText().toString() == null || etLen.getText().toString().isEmpty()) {
                    UIHelper.ToastMessage(mContext,"La longitud de los datos no puede estar vacia. ");
                    return;
                }
                if (etPtr.getText().toString() == null || etPtr.getText().toString().isEmpty()) {
                    UIHelper.ToastMessage(mContext, "La dirección inicial no puede estar vacía.");
                    return;
                }
                int ptr = StringUtils.toInt(etPtr.getText().toString(), 0);
                int len = StringUtils.toInt(etLen.getText().toString(), 0);
                String data = etData.getText().toString().trim();
                if (len > 0) {
                    String rex = "[\\da-fA-F]*"; //Coincidir con expresión regular, los datos están en formato hexadecimal
                    if (data == null || data.isEmpty() || !data.matches(rex)) {
                        UIHelper.ToastMessage(mContext,  getString(R.string.MSG_Filtro_hexadecimal));
                        return;
                    }

                    if (mContext.mReader.setFilter(filterBank, ptr, len, data)) {
                        UIHelper.ToastMessage(mContext, R.string.MSG_Filtro_exitoso);
                    } else {
                        UIHelper.ToastMessage(mContext, R.string.MSG_Fallo_en_el_filtro);
                    }
                } else {
                    //Desactivar filtrado
                    String dataStr = "";
                    if (mContext.mReader.setFilter(RFIDWithUHFUART.Bank_EPC, 0, 0, dataStr)
                            && mContext.mReader.setFilter(RFIDWithUHFUART.Bank_TID, 0, 0, dataStr)
                            && mContext.mReader.setFilter(RFIDWithUHFUART.Bank_USER, 0, 0, dataStr)) {
                        UIHelper.ToastMessage(mContext, R.string.MSG_Desabolitar_exito);
                    } else {
                        UIHelper.ToastMessage(mContext, R.string.MSG_Desabilitar_fallo);
                    }
                }
                cbFilter.setChecked(false);
            }
        });
        CheckBox cb_filter = (CheckBox) view.findViewById(R.id.cb_filter);
        rbEPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbEPC.isChecked()) {
                    etPtr.setText("32");
                }
            }
        });
        rbTID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbTID.isChecked()) {
                    etPtr.setText("0");
                }
            }
        });
        rbUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbUser.isChecked()) {
                    etPtr.setText("0");
                }
            }
        });
    }

    private void initEPCTamperAlarm (View view){
        cbEPC_Tam = (CheckBox) view.findViewById(R.id.cbEPC_Tam);
        cbEPC_Tam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //mContext.mReader.setEPCAndTamperAlarmMode();
                }else {
                    mContext.mReader.setEPCMode();
                }
            }
        });
    }

    @Override
    public void onPause(){
        Log.i(TAG, "Lector_C5_Fragment.onPause");
        super.onPause();
        // detener el reconocimiento
        //stopInventory();
    }




    /**
     * Agregar datos a la lista
     *
     * @param
     */
    private void addDataToList(String epc, String epcAndTidUser, String rssi) {
        if (StringUtils.isNotEmpty(epc)) {
            int index = checkIsExist(epc);
            map = new HashMap<String, String>();
            map.put(TAG_EPC, epc);
            map.put(TAG_EPC_TID, epcAndTidUser);
            map.put(TAG_COUNT, String.valueOf(1));
            map.put(TAG_RSSI, rssi);
            if (index == -1) {
                mContext.tagList.add(map);
                tempDatas.add(epc);
                tv_count.setText(String.valueOf(adapter.getCount()));
                EPC += epc + "+" ;
            }else {
                int tagCount = Integer.parseInt(mContext.tagList.get(index).get(TAG_COUNT), 10)+1;
                map.put(TAG_COUNT, String.valueOf(tagCount));
                map.put(TAG_EPC_TID, epcAndTidUser);
                // epcTidUser.add(epcAndTidUser);
                mContext.tagList.set(index, map);
            }
            tv_total.setText(String.valueOf(++total));
            adapter.notifyDataSetChanged();

            //---------------------------------------------
            mContext.uhfInfo.setTempDatas(tempDatas);
            mContext.uhfInfo.setTagList(mContext.tagList);
            mContext.uhfInfo.setCount(total);
            mContext.uhfInfo.setTagNumber(adapter.getCount());
        }
    }


    /**
     * Determinar si el EPC está en la lista
     *
     * @param epc índice (index)
     * @return
     */
    public int checkIsExist(String epc) {
        if (StringUtils.isEmpty(epc)) {
            return -1;
        }
        for (int k = 0; k < tempDatas.size(); k++) {
            if (epc.equals(tempDatas.get(k))) {
                return k;
            }
        }
        return -1;
    }

    //En esta parte se agregan los listeners de los botones
    public class BtClearClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            clearData();
            selectItem = -1;
            mContext.uhfInfo = new UhfInfo();
        }
    }

    //En esta parte se agregan los listeners de los botones
    public class BtInventarioClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (EPC.isEmpty()) {
                // Manejar el caso cuando EPC está vacío
                UIHelper.ToastMessage(mContext, "No hay EPCs disponibles.");
                return;
            }else {
//                contarActivosPorRFIDs(EPC);
            }
        }
    }

    public class BtInventoryClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            readTag();
        }
    }

    public class RgInventoryCheckedListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == RbInventorySingle.getId()) {
                // Identificación de un solo paso
                inventoryFlag = 0;
                cbFilter.setChecked(false);
                cbFilter.setVisibility(View.INVISIBLE);
            } else if (checkedId == RbInventoryLoop.getId()) {
                // Identificación de bucle de etiqueta única
                inventoryFlag = 1;
                cbFilter.setVisibility(View.VISIBLE);
            }
        }
    }



    ///Esta funcion limpia los textview y la lista
    private void clearData() {
        tv_count.setText("0");
        tv_total.setText("0");
        EPC = "";
        tvTime.setText("0s");
        total = 0;
        mContext.tagList.clear();
        tempDatas.clear();
        adapter.notifyDataSetChanged();
    }

    private void setViewEnabled (boolean enabled){
        RbInventorySingle.setEnabled(enabled);
        RbInventoryLoop.setEnabled(enabled);
        cbFilter.setEnabled(enabled);
        btnSetFilter.setEnabled(enabled);
        BtClear.setEnabled(enabled);
        cbEPC_Tam.setEnabled(enabled);
    }

    /**
     * detener el reconocimiento
     */
    private void stopInventory(){
        if(mContext.loopFlag){
            mContext.loopFlag = false;
            setViewEnabled(true);
            if(mContext.mReader.stopInventory()){
                BtInventory.setText(mContext.getString(R.string.btInventory));
            }else{
                UIHelper.ToastMessage(mContext, R.string.title_stop_Inventory);
            }
        }
    }

    public void readTag(){

        if (!isVisible()) {
            // Si el fragmento no está visible, no hacer nada.
            return;
        }

        cbFilter.setChecked(false);
        if (BtInventory.getText().equals(mContext.getString(R.string.btInventory))){ //etiqueta de identificación
            switch (inventoryFlag) {
                case 0:// Un solo TAG
                    time = System.currentTimeMillis();
                    UHFTAGInfo uhftagInfo = mContext.mReader.inventorySingleTag();
                    if (uhftagInfo != null) {
                        String tid = uhftagInfo.getTid();
                        String epc = uhftagInfo.getEPC();
                        String user=uhftagInfo.getUser();
                        addDataToList(epc,mergeTidEpc(tid, epc, user), uhftagInfo.getRssi());
                        setTotalTime();
                        mContext.playSound(1);
                    } else {
                        UIHelper.ToastMessage(mContext, R.string.MSG_Fallo_en_el_filtro);
//					    mContext.playSound(2);
                    }
                    break;
                case 1:// bucle de etiqueta única
                    mContext.mReader.setInventoryCallback(new IUHFInventoryCallback() {
                        @Override
                        public void callback(UHFTAGInfo uhftagInfo) {
                            Message msg = handler.obtainMessage();
                            msg.obj = uhftagInfo;
                            msg.what=1;
                            handler.sendMessage(msg);
                            mContext.playSound(1);
                        }
                    });
                    if (mContext.mReader.startInventoryTag()) {
                        BtInventory.setText(mContext.getString(R.string.title_stop_Inventory));
                        mContext.loopFlag = true;
                        setViewEnabled(false);
                        time = System.currentTimeMillis();
                        handler.sendEmptyMessageDelayed(2,10);
                    } else {
                        stopInventory();
                        UIHelper.ToastMessage(mContext, R.string.MSG_Fallo_en_el_filtro);
                    }
                    break;
                default:
                    break;
            }

        }else {// detener el reconocimiento
            stopInventory();
            setTotalTime();
        }




    }

    private void setTotalTime() {
        float useTime = (System.currentTimeMillis() - time) / 1000.0F;
        tvTime.setText(NumberTool.getPointDouble(1, useTime) + "s");
    }

    @Override
    public void myOnKeyDwon(){
        readTag();    }

    private String mergeTidEpc(String tid, String epc,String user) {
        epcTidUser.add(epc);
        String data="EPC:"+ epc;
        if (!TextUtils.isEmpty(tid) && !tid.equals("0000000000000000") && !tid.equals("000000000000000000000000")) {
            epcTidUser.add(tid);
            data+= "\nTID:" + tid ;
        }
        if(user!=null && user.length()>0) {
            epcTidUser.add(user);
            data+="\nUSER:"+user;
        }
        return  data;
    }


    //-----------------------------
    private int selectItem = -1;

    public final class ViewHolder {
        public TextView tvEPCTID;
        public TextView tvTagCount;
        public TextView tvTagRssi;
    }
//
//    private void contarActivosPorRFIDs(String EPC) {
//        DialogoAnimaciones.showLoadingDialog(getContext());
//        String tags = EPC.substring(0, EPC.length() - 1);
//        // Asignación de datos para el webservice / mapa de parámetros que se enviarán al webservice
//        Map<String, String> propeties = new HashMap<>();
//        propeties.put("cadenaRFIDs", tags);
//
//        webServiceManager.callWebService("ContarActivosPorRFIDs", propeties, new WebServiceManager.WebServiceCallback() {
//            @Override
//            public void onWebServiceCallComplete(String result) {
//                try {
//                    DialogoAnimaciones.hideLoadingDialog();
//                    if (result.equals("[]") || result.contains("Error") || result.contains("Time out") || result.contains("Failed to connect to")) {
//                        // Show the no internet dialog on connection error
//                        showNoInternetDialog("Fallo de conexion \n Clave IH-612",EPC);
//
//                    } else {
//                        String valor = result;
//                        clearData();
//
//                        // Crear el Bundle y añadir el valor
//                        Bundle bundle = new Bundle();
//                        bundle.putString("valor", valor);
//
//                        // Obtener el NavController desde el fragmento actual
//                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
//
//                        // Navegar al fragmento registroFragment pasando el bundle con datos
//                        navController.navigate(R.id.action_nav_inventario_to_nav_inventario2, bundle);
//                    }
//                }catch (Exception e) {
//                    DialogoAnimaciones.hideLoadingDialog();
//                    // Show the no internet dialog on exception
//                    showNoInternetDialog(e.getMessage(), EPC);
//                }
//
//            }
//
//
//        });
//    }
//
//    // Función para mostrar el diálogo sin internet.
//    private void showNoInternetDialog(String errorMessage, String EPC_a) {
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.carga, null);
//
//        //androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext(), R.style.FullScreenDialog);
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
//        builder.setView(dialogView);
//
//        androidx.appcompat.app.AlertDialog dialog = builder.create();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        dialog.show();
//
//        // Set the error message in the dialog if you have a TextView for it
//        TextView errorTextView = dialogView.findViewById(R.id.TV_MsgCarga);
//        if (errorTextView != null) {
//            errorTextView.setText(errorMessage);
//        }
//
//        Button closeButton = dialogView.findViewById(R.id.BT_voverint);
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                contarActivosPorRFIDs(EPC_a); // Call the web service again when the button is clicked
//            }
//        });
    }

