package com.manueldev.roghurgarantias.ui.clients;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.manueldev.roghurgarantias.R;
import com.manueldev.roghurgarantias.apis.AdministrationAssuranceApi;
import com.manueldev.roghurgarantias.models.ClientDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.manueldev.roghurgarantias.helpers.Utils.ENDPOINT_BASE_ADM_ASSURANCES_IIS_PROD;

public class ClientFragment extends Fragment {

    private ClientViewModel clientViewModel;

    private EditText etClientDni, etClientNames, etClientLastNames, etMobilePhone;
    private Button btnSaveClient;


    private Retrofit retrofit;
    private AdministrationAssuranceApi administrationAssurancesApi;
    private Call<ClientDTO> registerClientCallApi;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        clientViewModel =
                ViewModelProviders.of(this).get(ClientViewModel.class);
        View root = inflater.inflate(R.layout.fragment_clients, container, false);
        /*final TextView textView = root.findViewById(R.id.text_clients);
        clientViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        etClientDni = root.findViewById(R.id.et_dni_register_clients);
        etClientNames = root.findViewById(R.id.et_names_register_clients);
        etClientLastNames = root.findViewById(R.id.et_lastnames_register_clients);
        etMobilePhone = root.findViewById(R.id.et_phone_register_clients);

        btnSaveClient = root.findViewById(R.id.btn_save_register_clients);


        retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT_BASE_ADM_ASSURANCES_IIS_PROD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        administrationAssurancesApi = retrofit.create(AdministrationAssuranceApi.class);

        btnSaveClient.setOnClickListener(view -> {

            List<String> errorsFormClient = validformRegisterClient();
            if (errorsFormClient.isEmpty()) {
                ClientDTO clientDTO = new ClientDTO()
                        .setDni(etClientDni.getText().toString())
                        .setNames(etClientNames.getText().toString())
                        .setLastnames(etClientLastNames.getText().toString())
                        .setMobilePhone(etMobilePhone.getText().toString());

                registerClientCallApi = administrationAssurancesApi.registerClient(clientDTO);
                registerClientCallApi.enqueue(new Callback<ClientDTO>() {

                    @Override
                    public void onResponse(Call<ClientDTO> call, Response<ClientDTO> response) {

                        etClientDni.getText().clear();
                        etClientNames.getText().clear();
                        etClientLastNames.getText().clear();
                        etMobilePhone.getText().clear();

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Roghur Garantias");
                        builder.setMessage("Cliente registrado satisfactoriamente");
                        builder.setPositiveButton("Aceptar", null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    @Override
                    public void onFailure(Call<ClientDTO> call, Throwable t) {

                    }
                });
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("ROGHUR Garantias");

                StringBuilder sb = new StringBuilder();
                for (int i =0; i<errorsFormClient.size();i++) {
                    sb.append(errorsFormClient.get(i));
                    if (i< errorsFormClient.size()-1){
                        sb.append(System.getProperty("line.separator"))
                            .append(System.getProperty("line.separator"));
                    }
                };
                builder.setMessage(sb.toString());
                builder.setPositiveButton("Aceptar", null);

                AlertDialog dialog = builder.create();
                dialog.setIcon(R.drawable.ic_baseline_error_24);
                dialog.show();
            }


        });


        return root;
    }

    private List<String> validformRegisterClient() {
        List<String> errosFormClient = new ArrayList<>();
        if (TextUtils.isEmpty(etClientDni.getText())) {
            errosFormClient.add("1. Debe ingresar CI, es un campo obligatorio");
        }
        if (TextUtils.isEmpty(etClientNames.getText())) {
            errosFormClient.add("2. Debe ingresar Nombre, es un campo obligatorio");
        }
        if (TextUtils.isEmpty(etClientLastNames.getText())) {
            errosFormClient.add("3. Debe ingresar Apellido, es un campo obligatorio");
        }
        return errosFormClient;
    }
}