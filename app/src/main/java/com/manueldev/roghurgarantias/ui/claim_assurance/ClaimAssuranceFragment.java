package com.manueldev.roghurgarantias.ui.claim_assurance;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.manueldev.roghurgarantias.R;
import com.manueldev.roghurgarantias.adapters.AssurancesAdapter;
import com.manueldev.roghurgarantias.apis.AdministrationAssuranceApi;
import com.manueldev.roghurgarantias.models.ActiveAssuranceDTO;
import com.manueldev.roghurgarantias.models.AssuranceDTO;
import com.manueldev.roghurgarantias.models.ClaimAssuranceDTO;
import com.manueldev.roghurgarantias.models.ClientDTO;
import com.manueldev.roghurgarantias.preferences.AccessUserSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.manueldev.roghurgarantias.helpers.Utils.*;

public class ClaimAssuranceFragment extends Fragment implements AssurancesAdapter.ItemViewClickListener {

    private ClaimAssuranceViewModel claimAssuranceViewModel;

    private RecyclerView rvAssurancesActivated;
    private AssurancesAdapter assuranceHistoryAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayout linearLayoutParent;
    private ImageButton btnFindClient;
    private LinearLayout llAssurancesEmpty;
    private EditText etClientDniForSearch;
    private TextView tvGetClientDni, tvGetClientFullname;
    private CardView cvClientData;

    private Retrofit retrofit;
    private AdministrationAssuranceApi administrationAssurancesApi;

    private Call<ClientDTO> findClientCallApi;
    private Call<List<ActiveAssuranceDTO>> findAllAssurancesByClientCallApi;
    private Call<AssuranceDTO> claimAssuranceCallApi;

    private List<ActiveAssuranceDTO> assurances;
    private long clientFindId;

    private AccessUserSharedPreferences accessUserSharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        claimAssuranceViewModel =
                ViewModelProviders.of(this).get(ClaimAssuranceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_claim_assurance, container, false);
        /*final TextView textView = root.findViewById(R.id.text_claim_assurance);
        claimAssuranceViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        swipeRefreshLayout = root.findViewById(R.id.swr_reload_claims_assurances);
        btnFindClient = root.findViewById(R.id.btn_find_client_claim_assurance);
        etClientDniForSearch = root.findViewById(R.id.et_client_dni_claim_assurance);
        tvGetClientDni = root.findViewById(R.id.tv_dni_get_client_claim_assurance);
        tvGetClientFullname = root.findViewById(R.id.tv_fullname_get_client_claim_assurance);
        linearLayoutParent = root.findViewById(R.id.ll_claim_assurance);
        cvClientData = root.findViewById(R.id.card_client_data_activate_assurance);
        llAssurancesEmpty = root.findViewById(R.id.ll_for_assurances_empty_claim_assurance);

        rvAssurancesActivated = root.findViewById(R.id.rv_claims_assurances);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());


        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(rvAssurancesActivated.getContext(),
                linearLayoutManager.getOrientation());
        rvAssurancesActivated.addItemDecoration(mDividerItemDecoration);

        rvAssurancesActivated.setLayoutManager(linearLayoutManager);

        cvClientData = root.findViewById(R.id.card_client_data_claim_assurance);
        cvClientData.setVisibility(View.GONE);

        retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT_BASE_ADM_ASSURANCES_IIS_PROD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        administrationAssurancesApi = retrofit.create(AdministrationAssuranceApi.class);

        accessUserSharedPreferences = new AccessUserSharedPreferences(getContext());

        assurances = new ArrayList<>();
        assuranceHistoryAdapter = new AssurancesAdapter(getContext(), ClaimAssuranceFragment.this, assurances);
        rvAssurancesActivated.setAdapter(assuranceHistoryAdapter);

        btnFindClient.setOnClickListener(view -> {
            findClientCallApi = administrationAssurancesApi.searchClientByDni(etClientDniForSearch.getText().
                    toString());

            findClientCallApi.enqueue(new Callback<ClientDTO>() {
                @Override
                public void onResponse(Call<ClientDTO> call, Response<ClientDTO> response) {
                    ClientDTO clientFind = response.body();
                    boolean existsClient = clientFind != null;
                    if (existsClient) {
                        tvGetClientDni.setText(clientFind.getDni());
                        tvGetClientFullname.setText(clientFind.getFullName());

                        // Desacoplar las llamadas anaidadas
                        clientFindId = clientFind.getClientId();
                        findAllAssurancesByClientCallApi = administrationAssurancesApi.getAllAssurancesByClientId(clientFindId);
                        findAllAssurancesByClientCallApi.enqueue(new Callback<List<ActiveAssuranceDTO>>() {
                            @Override
                            public void onResponse(Call<List<ActiveAssuranceDTO>> call, Response<List<ActiveAssuranceDTO>> response) {
                                assurances = response.body();
                                /*if (assurances.isEmpty()) {
                                    assurances.clear();
                                    assuranceHistoryAdapter.notifyItemRangeRemoved(0 , assurances.size());
                                }*/
                                assuranceHistoryAdapter.setAssurances(assurances);
                                assuranceHistoryAdapter.notifyDataSetChanged();
                                cleanViewAssurancesList();
                            }

                            @Override
                            public void onFailure(Call<List<ActiveAssuranceDTO>> call, Throwable t) {

                            }
                        });
                    } else {
                        assurances.clear();
                        assuranceHistoryAdapter.setAssurances(assurances);
                        assuranceHistoryAdapter.notifyDataSetChanged();
                        //assuranceHistoryAdapter.notifyItemRangeRemoved(0 , assurances.size());
                    }
                    showClientData(existsClient);
                }

                @Override
                public void onFailure(Call<ClientDTO> call, Throwable t) {

                }
            });
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Desacoplar las llamadas anaidadas
            findAllAssurancesByClientCallApi = administrationAssurancesApi.getAllAssurancesByClientId(clientFindId);
            findAllAssurancesByClientCallApi.enqueue(new Callback<List<ActiveAssuranceDTO>>() {
                @Override
                public void onResponse(Call<List<ActiveAssuranceDTO>> call, Response<List<ActiveAssuranceDTO>> response) {
                    assurances.clear();
                    assurances = response.body();
                    assuranceHistoryAdapter.setAssurances(assurances);
                    assuranceHistoryAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(Boolean.FALSE);
                }

                @Override
                public void onFailure(Call<List<ActiveAssuranceDTO>> call, Throwable t) {

                }
            });

        });
        return root;
    }

    private void cleanViewAssurancesList() {
        boolean anyAssurance =!assurances.isEmpty();
        if (anyAssurance){
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            llAssurancesEmpty.setVisibility(View.GONE);
        } else {
            swipeRefreshLayout.setVisibility(View.GONE);
            llAssurancesEmpty.setVisibility(View.VISIBLE);
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
        cvClientData.setVisibility(existsClient ? View.VISIBLE : View.GONE);
        cleanViewAssurancesList();
    }

    @Override
    public void onClickItemView(int position) {
        ActiveAssuranceDTO activeAssuranceDTO = assurances.get(position);

        if (activeAssuranceDTO.getAssuranceState().equals(ASSURANCE_ACTIVED)) {

            final Dialog claimAssuranceDialog = new Dialog(getContext());
            claimAssuranceDialog.setTitle("ROGHUR Garantias");
            claimAssuranceDialog.setContentView(R.layout.claim_assurance_dialog);
            final EditText etReasonClaimAssuranceByDialog = (EditText) claimAssuranceDialog.findViewById(R.id.et_reason_claim_assurance_dialog);
            Button btnClaimAssurance = (Button) claimAssuranceDialog.findViewById(R.id.btn_save_claim_assurance_dialog);

            int width = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.9);
            int height = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.5);

            claimAssuranceDialog.getWindow().setLayout(width, height);
            claimAssuranceDialog.setCancelable(true);
            claimAssuranceDialog.show();

            btnClaimAssurance.setOnClickListener(view -> {
                ClaimAssuranceDTO claimAssuranceDTO = new ClaimAssuranceDTO()
                        .setAssuranceId(activeAssuranceDTO.getAssuranceId())
                        .setUsersCommerceClaimId(accessUserSharedPreferences.getUserIdLogged()); // TODO: introducir el login

                if (!TextUtils.isEmpty(etReasonClaimAssuranceByDialog.getText())) {
                    claimAssuranceDTO.setReason(etReasonClaimAssuranceByDialog.getText().toString());
                }
                claimAssuranceCallApi = administrationAssurancesApi.claimAssurance(claimAssuranceDTO);
                claimAssuranceCallApi.enqueue(new Callback<AssuranceDTO>() {
                    @Override
                    public void onResponse(Call<AssuranceDTO> call, Response<AssuranceDTO> response) {
                        AssuranceDTO assurance = response.body();
                        if (assurance != null) {
                            Toast.makeText(getContext(), "Garantia Ejecutada correctamente!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Garantia no fue ejecutada correctamente, intente nuevamente", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AssuranceDTO> call, Throwable t) {
                    }
                });
                claimAssuranceDialog.dismiss();
            });


        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("ROGHUR Garantias")
                    .setMessage("La garantia ya fue ejecutada anteriormente")
                    .setIcon(R.drawable.ic_baseline_warning_24)
                    .setPositiveButton("Aceptar", null)
                    .show();

        }





    }
}