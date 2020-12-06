package com.manueldev.roghurgarantias.ui.activate_assurance;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.zxing.common.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.manueldev.roghurgarantias.R;
import com.manueldev.roghurgarantias.apis.AdministrationAssuranceApi;
import com.manueldev.roghurgarantias.models.AssuranceDTO;
import com.manueldev.roghurgarantias.models.ClientDTO;
import com.manueldev.roghurgarantias.models.ProductDTO;
import com.manueldev.roghurgarantias.preferences.AccessUserSharedPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.manueldev.roghurgarantias.helpers.Utils.ENDPOINT_BASE_ADM_ASSURANCES_IIS_PROD;
import static com.manueldev.roghurgarantias.helpers.Utils.VALUE_NOT_ASSIGNED_ID;

public class ActivateAssuranceFragment extends Fragment {

    private View view;
    private Activity paractiActivity;


    private ActivateAssuranceViewModel activateAssuranceViewModel;

    private Button btnActivateAssurance;
    private EditText etClientDniForSearch;
    private TextView tvGetClientDni, tvGetClientFullname;
    private TextView tvGetProductCode, tvGetProductName, tvGetProductMark, tvGetProductDaysExpiration;
    private ImageButton btnFindClient, btnSearchProductByCodeDialog, btnSarchProductByBarcodeEvent;
    private LinearLayout linearLayoutParent;

    private CardView cvClientFindData, cvProductFindData;

    private Retrofit retrofit;
    private AdministrationAssuranceApi administrationAssurancesApi;
    private Call<ClientDTO> findClientCallApi;
    private Call<ProductDTO> findProductCallApi;
    private Call<AssuranceDTO> activateAssuranceCallApi;

    private long productFindId, clientFindId, userCommerceId, daysExpiration;
    private String serialCodeProductInBarCode;

    private AccessUserSharedPreferences accessUserSharedPreferences;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        paractiActivity = getActivity();

        activateAssuranceViewModel = ViewModelProviders.of(this).get(ActivateAssuranceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_activate_assurance, container, false);

        view = root;

        linearLayoutParent = view.findViewById(R.id.ll_activate_assurance);

        etClientDniForSearch = view.findViewById(R.id.et_client_dni_activate_assurances);
        tvGetClientDni = view.findViewById(R.id.tv_dni_get_client_activate_assurance);
        tvGetClientFullname = view.findViewById(R.id.tv_fullname_get_client_activate_assurance);

        tvGetProductCode = view.findViewById(R.id.tv_code_get_product_active_assurance);
        tvGetProductName = view.findViewById(R.id.tv_name_get_product_active_assurance);
        tvGetProductMark = view.findViewById(R.id.tv_mark_get_product_active_assurance);
        tvGetProductDaysExpiration = view.findViewById(R.id.tv_days_expiration_get_product_active_assurance);

        btnFindClient = view.findViewById(R.id.btn_find_client_activate_assurance);
        btnSearchProductByCodeDialog = root.findViewById(R.id.btn_search_product_by_code_activate_assurance);
        btnSarchProductByBarcodeEvent = root.findViewById(R.id.btn_search_product_by_barcode_activate_assurance);

        cvClientFindData = view.findViewById(R.id.card_client_data_activate_assurance);
        cvClientFindData.setVisibility(View.GONE);

        cvProductFindData = view.findViewById(R.id.card_product_data_activate_assurance);
        cvProductFindData.setVisibility(View.GONE);

        btnActivateAssurance = view.findViewById(R.id.btn_save_activate_assurance);

        accessUserSharedPreferences = new AccessUserSharedPreferences(getContext());

        productFindId = VALUE_NOT_ASSIGNED_ID;
        clientFindId = VALUE_NOT_ASSIGNED_ID;
        serialCodeProductInBarCode = "";

        userCommerceId = accessUserSharedPreferences.getUserIdLogged(); // TODO ID de inicio de sesion

        activateActionActivaAssurance();

        retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT_BASE_ADM_ASSURANCES_IIS_PROD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        administrationAssurancesApi = retrofit.create(AdministrationAssuranceApi.class);


        btnFindClient.setOnClickListener(view -> {

            findClientCallApi = administrationAssurancesApi.searchClientByDni(etClientDniForSearch.getText().
                    toString());

            findClientCallApi.enqueue(new Callback<ClientDTO>() {
                @Override
                public void onResponse(Call<ClientDTO> call, Response<ClientDTO> response) {
                    ClientDTO clientFind = response.body();
                    boolean existsClient = clientFind != null;
                    if (existsClient) {
                        clientFindId = clientFind.getClientId();
                        tvGetClientDni.setText(clientFind.getDni());
                        tvGetClientFullname.setText(clientFind.getFullName());
                    }
                    showClientData(existsClient);
                }

                @Override
                public void onFailure(Call<ClientDTO> call, Throwable t) {

                }
            });


        });

        btnSearchProductByCodeDialog.setOnClickListener(view -> {
            //final String value=(String)dataSnapshot.getValue();
            final Dialog searchProductByCodeDialog = new Dialog(getContext());
            searchProductByCodeDialog.setTitle("ROGHUR Garantias");
            searchProductByCodeDialog.setContentView(R.layout.search_product_by_code_dialog);
            final EditText etCodeProductByDialog = (EditText) searchProductByCodeDialog.findViewById(R.id.et_code_product_search_dialog);
            Button btnSearchProduct = (Button) searchProductByCodeDialog.findViewById(R.id.btn_search_produyt_dialog);
            int width = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.9);
            int height = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.38);

            searchProductByCodeDialog.getWindow().setLayout(width, height);

            searchProductByCodeDialog.setCancelable(true);
            btnSearchProduct.setOnClickListener(v -> {
                String productCode = etCodeProductByDialog.getText().toString().trim();

                callFindProductByAnyCode(productCode, 1);




                /*if (dato.equals(value)) {
                    sharedPreferences.edit().putString(ARG_TIPO_USER,"admin").commit();
                } else {
                    logoutApp(sharedPreferences.getString("access",""));
                }*/
                searchProductByCodeDialog.cancel();
            });
            searchProductByCodeDialog.show();
        });

        btnSarchProductByBarcodeEvent.setOnClickListener(view -> {
            IntentIntegrator.forSupportFragment(ActivateAssuranceFragment.this)
                    .initiateScan();
            //new IntentIntegrator(getActivity()).initiateScan();
        });

        btnActivateAssurance.setOnClickListener(v -> {
            AssuranceDTO assurance = new AssuranceDTO()
                    .setClientId(clientFindId)
                    .setProductId(productFindId)
                    .setUsersCommerceActivateId(userCommerceId)
                    .setExpirationDays(daysExpiration);

            activateAssuranceCallApi = administrationAssurancesApi.activeAssurance(assurance);
            activateAssuranceCallApi.enqueue(new Callback<AssuranceDTO>() {
                @Override
                public void onResponse(Call<AssuranceDTO> call, Response<AssuranceDTO> response) {
                    AssuranceDTO assuranceActivated = response.body();
                    if (assuranceActivated != null) {
                        Toast.makeText(paractiActivity, "Garantia Activada!", Toast.LENGTH_SHORT).show();
                        cleanParamsActivateAssurance();
                    } else {
                        Toast.makeText(paractiActivity, "La Garantia no fue Activada, intente nuevamente", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<AssuranceDTO> call, Throwable t) {
                    String error = t.getCause().getMessage();
                    Toast.makeText(paractiActivity, error, Toast.LENGTH_SHORT).show();
                }
            });
        });
        return root;
    }

    private void cleanParamsActivateAssurance() {
        etClientDniForSearch.getText().clear();
        cvClientFindData.setVisibility(View.GONE);
        cvProductFindData.setVisibility(View.GONE);
        clientFindId = VALUE_NOT_ASSIGNED_ID;
        productFindId = VALUE_NOT_ASSIGNED_ID;
        activateActionActivaAssurance();

    }

    public void callFindProductByAnyCode(String value, int codeType){
        if (codeType == 1){ // produtCode textual
            findProductCallApi = administrationAssurancesApi.getProductByCodeOrSerial(value, null);
        } else { // serialCode scanner barcode
            findProductCallApi = administrationAssurancesApi.getProductByCodeOrSerial(null, value);
        }

        findProductCallApi.enqueue(new Callback<ProductDTO>() {
            @Override
            public void onResponse(Call<ProductDTO> call, Response<ProductDTO> response) {
                ProductDTO productFind = response.body();
                boolean existsProduct = productFind != null;
                if (existsProduct) {
                    productFindId = productFind.getProductId();
                    tvGetProductCode.setText(productFind.getCodeArticle());
                    tvGetProductName.setText(productFind.getName());
                    tvGetProductMark.setText(productFind.getMark());

                    daysExpiration = productFind.getExpirationDays();
                    tvGetProductDaysExpiration.setText(String.valueOf(daysExpiration));
                }
                showProductData(existsProduct);
            }

            @Override
            public void onFailure(Call<ProductDTO> call, Throwable t) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
            if (result.getContents() != null){
                //Toast.makeText(paractiActivity, "El código de barras es:\n" + result.getContents(), Toast.LENGTH_SHORT).show();
                serialCodeProductInBarCode = result.getContents().trim();

                callFindProductByAnyCode(serialCodeProductInBarCode, 2);
            }else{
                Toast.makeText(paractiActivity, "Error al escanear el codigo de barras", Toast.LENGTH_SHORT).show();
            }
    }

    public void showClientData(boolean existsClient) {
        Transition transition = new Fade();
        if (existsClient) {
            transition.setDuration(400);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Roghur Garantias");
            builder.setMessage("El Cliente no fue encontrado, consulte nuevamente");
            builder.setPositiveButton("Aceptar", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            transition.setDuration(200);

            etClientDniForSearch.getText().clear();
        }
        transition.addTarget(R.id.image);
        TransitionManager.beginDelayedTransition(linearLayoutParent, transition);
        cvClientFindData.setVisibility(existsClient ? View.VISIBLE : View.GONE);
        activateActionActivaAssurance();
    }

    public void showProductData(boolean existsProduct) {
        Transition transition = new Fade();
        if (existsProduct) {
            transition.setDuration(400);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Roghur Garantias");
            builder.setMessage("El Producto no fue encontrado, consulte nuevamente");
            builder.setPositiveButton("Aceptar", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            transition.setDuration(200);
        }
        transition.addTarget(R.id.image);
        TransitionManager.beginDelayedTransition(linearLayoutParent, transition);
        cvProductFindData.setVisibility(existsProduct ? View.VISIBLE : View.GONE);
        activateActionActivaAssurance();

    }

    public void activateActionActivaAssurance() {
        boolean canActive = productFindId != VALUE_NOT_ASSIGNED_ID && clientFindId != VALUE_NOT_ASSIGNED_ID;
        if (!canActive) {
            btnActivateAssurance.setBackgroundColor(getResources().getColor(R.color.colorGray));
        } else {
            btnActivateAssurance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        btnActivateAssurance.setEnabled(canActive);
    }


}