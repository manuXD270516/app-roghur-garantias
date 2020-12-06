package com.manueldev.roghurgarantias.ui.assurances_history;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.manueldev.roghurgarantias.R;
import com.manueldev.roghurgarantias.adapters.AssurancesAdapter;
import com.manueldev.roghurgarantias.apis.AdministrationAssuranceApi;
import com.manueldev.roghurgarantias.helpers.LoadingDialog;
import com.manueldev.roghurgarantias.models.ActiveAssuranceDTO;
import com.manueldev.roghurgarantias.models.AssuranceDTO;
import com.manueldev.roghurgarantias.models.ClaimAssuranceDTO;
import com.manueldev.roghurgarantias.preferences.AccessUserSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.manueldev.roghurgarantias.helpers.Utils.*;

public class AssurancesHistoryFragment extends Fragment implements AssurancesAdapter.ItemViewClickListener {

    private AssuranceHistoryViewModel assuranceHistoryViewModel;

    private RecyclerView rvAssurances;
    private AssurancesAdapter assuranceHistoryAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadingDialog loadingDialog;
    private LinearLayout llAssurancesEmpty;

    private List<ActiveAssuranceDTO> assurances;

    private Retrofit retrofit;
    private AdministrationAssuranceApi administrationAssurancesApi;
    private Call<List<ActiveAssuranceDTO>> assurancesCallApi;
    private Call<AssuranceDTO> claimAssuranceCallApi;

    private AccessUserSharedPreferences accessUserSharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        assuranceHistoryViewModel =
                ViewModelProviders.of(this).get(AssuranceHistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_assurance_history, container, false);

        swipeRefreshLayout = root.findViewById(R.id.swr_reload_assurance_history);

        rvAssurances = root.findViewById(R.id.rv_assurances_history);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());


        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(rvAssurances.getContext(),
                linearLayoutManager.getOrientation());
        rvAssurances.addItemDecoration(mDividerItemDecoration);
        rvAssurances.setLayoutManager(linearLayoutManager);

        llAssurancesEmpty = root.findViewById(R.id.ll_for_assurances_empty_assurances_history);

        accessUserSharedPreferences = new AccessUserSharedPreferences(getContext());

        retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT_BASE_ADM_ASSURANCES_IIS_PROD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        administrationAssurancesApi = retrofit.create(AdministrationAssuranceApi.class);

        assurances = new ArrayList<>();

        loadingDialog = new LoadingDialog(getActivity(), "Cargando Garantias");
        loadingDialog.startLoadingDialog();

        assurancesCallApi = administrationAssurancesApi.getAllAssurancesHistory();
        assurancesCallApi.enqueue(new Callback<List<ActiveAssuranceDTO>>() {

            @Override
            public void onResponse(Call<List<ActiveAssuranceDTO>> call, Response<List<ActiveAssuranceDTO>> response) {
                loadingDialog.dismiss();
                assurances = response.body();
                assuranceHistoryAdapter = new AssurancesAdapter(getContext(), AssurancesHistoryFragment.this, assurances);
                rvAssurances.setAdapter(assuranceHistoryAdapter);
                cleanViewAssurancesList();
            }

            @Override
            public void onFailure(Call<List<ActiveAssuranceDTO>> call, Throwable t) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            //pbLoadingAssurance.setContentDescription("Cargando las garantias... Espere porfavor");

            assurancesCallApi = administrationAssurancesApi.getAllAssurancesHistory();
            assurancesCallApi.enqueue(new Callback<List<ActiveAssuranceDTO>>() {

                @Override
                public void onResponse(Call<List<ActiveAssuranceDTO>> call, Response<List<ActiveAssuranceDTO>> response) {

                    assurances = response.body();
                    assuranceHistoryAdapter.setAssurances(assurances);
                    assuranceHistoryAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(Boolean.FALSE);
                    cleanViewAssurancesList();
                }

                @Override
                public void onFailure(Call<List<ActiveAssuranceDTO>> call, Throwable t) {

                }
            });
        });
        swipeRefreshLayout.setRefreshing(false);

        return root;
    }

    private void cleanViewAssurancesList() {
        boolean anyAssurance = !assurances.isEmpty();
        if (anyAssurance) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            llAssurancesEmpty.setVisibility(View.GONE);
        } else {
            swipeRefreshLayout.setVisibility(View.GONE);
            llAssurancesEmpty.setVisibility(View.VISIBLE);
        }
    }

    // Ver el taller SOLID ducks para analizar la solucion correcta
    @Override
    public void onClickItemView(int position) {
        ActiveAssuranceDTO activeAssuranceDTO = assurances.get(position);
        // Validar si se puede ejecutar la garantia
        if (activeAssuranceDTO.assuranceExpired()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("ROGHUR Garantias")
                    .setMessage("La garantia del producto no puede ser activada, ya expiro, contacte con administracion")
                    .setIcon(R.drawable.ic_baseline_warning_24)
                    .setPositiveButton("Aceptar", null)
                    .show();
            return;
        }

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