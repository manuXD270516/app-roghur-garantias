package com.manueldev.roghurgarantias.ui.users_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.manueldev.roghurgarantias.LoginActivity;
import com.manueldev.roghurgarantias.R;
import com.manueldev.roghurgarantias.apis.AdministrationAssuranceApi;
import com.manueldev.roghurgarantias.enums.UserAppStatus;
import com.manueldev.roghurgarantias.models.AssuranceDTO;
import com.manueldev.roghurgarantias.models.ClientDTO;
import com.manueldev.roghurgarantias.models.ProductDTO;
import com.manueldev.roghurgarantias.models.ProfileUserCommerceDTO;
import com.manueldev.roghurgarantias.preferences.AccessUserSharedPreferences;

import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.manueldev.roghurgarantias.helpers.Utils.ENDPOINT_BASE_ADM_ASSURANCES_IIS_PROD;

public class UsersCommerceFragment extends Fragment {

    private UsersCommerceViewModel usersCommerceViewModel;
    private AccessUserSharedPreferences accessUserSharedPreferences;

    private Button btnSignOut;
    private TextView tvCountAssurancesActivated, tvCountAssurancesClaim, tvCountClientsRegistered;
    private TextView tvCommereWorkUserCommerce, tvCommerceAddressUserCommerce, tvUsernameUserCommerce;

    private Retrofit retrofit;
    private AdministrationAssuranceApi administrationAssurancesApi;
    private Call<ProfileUserCommerceDTO> getProfileCallApi;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        usersCommerceViewModel =
                ViewModelProviders.of(this).get(UsersCommerceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_users_commerce, container, false);

        tvCountAssurancesActivated = root.findViewById(R.id.tv_count_assurances_activated_profile_user_commerce);
        tvCountAssurancesClaim = root.findViewById(R.id.tv_count_assurances_claim_profile_user_commerce);
        tvCountClientsRegistered = root.findViewById(R.id.tv_count_clients_registered_profile_user_commerce);
        tvCommereWorkUserCommerce = root.findViewById(R.id.tv_commerce_work_profile_user_commerce);
        tvCommerceAddressUserCommerce = root.findViewById(R.id.tv_commerce_address_profile_user_commerce);
        tvUsernameUserCommerce = root.findViewById(R.id.tv_username_profile_user_commerce);

        btnSignOut = root.findViewById(R.id.btn_signout_profile_users_commerce);
        btnSignOut.setOnClickListener(view -> {

            accessUserSharedPreferences.logOutUser();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });


        retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT_BASE_ADM_ASSURANCES_IIS_PROD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        accessUserSharedPreferences = new AccessUserSharedPreferences(getContext());

        administrationAssurancesApi = retrofit.create(AdministrationAssuranceApi.class);
        getProfileCallApi = administrationAssurancesApi.getProfileUserCommerce(accessUserSharedPreferences.getUserIdLogged()); // TODO cambiar esto por el ID de usuario
        getProfileCallApi.enqueue(new Callback<ProfileUserCommerceDTO>() {
            @Override
            public void onResponse(Call<ProfileUserCommerceDTO> call, Response<ProfileUserCommerceDTO> response) {
                ProfileUserCommerceDTO profile = response.body();
                tvCountAssurancesActivated.setText(String.valueOf(profile.getCountAssurancesActivated()));
                tvCountAssurancesClaim.setText(String.valueOf(profile.getCountAssurancesClaim()));
                tvCountClientsRegistered.setText(String.valueOf(profile.getCountClientsRegister()));
                tvCommereWorkUserCommerce.setText(profile.getCommmerceWork());
                tvCommerceAddressUserCommerce.setText(profile.getCommerceAddress());
                tvUsernameUserCommerce.setText(profile.getUsername());

            }

            @Override
            public void onFailure(Call<ProfileUserCommerceDTO> call, Throwable t) {

            }
        });


        return root;
    }
}