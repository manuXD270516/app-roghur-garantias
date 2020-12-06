package com.manueldev.roghurgarantias;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.manueldev.roghurgarantias.apis.AdministrationAssuranceApi;
import com.manueldev.roghurgarantias.enums.UserAppStatus;
import com.manueldev.roghurgarantias.models.AuthenticationDTO;
import com.manueldev.roghurgarantias.models.UserLoginDTO;
import com.manueldev.roghurgarantias.preferences.AccessUserSharedPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.manueldev.roghurgarantias.helpers.Utils.ENDPOINT_BASE_ADM_ASSURANCES_IIS_PROD;

public class LoginActivity extends AppCompatActivity {


    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    private AccessUserSharedPreferences accessUserSharedPreferences;

    private EditText etUserName, etPassword;

    private Retrofit retrofit;
    private AdministrationAssuranceApi administrationAssurancesApi;
    private Call<AuthenticationDTO> authenticateCallApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName = findViewById(R.id.et_username_login);
        etPassword = findViewById(R.id.et_password_login);

        retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT_BASE_ADM_ASSURANCES_IIS_PROD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        administrationAssurancesApi = retrofit.create(AdministrationAssuranceApi.class);

        accessUserSharedPreferences = new AccessUserSharedPreferences(this);

        verifyUserAppStatus();
    }

    private void verifyUserAppStatus() {
        if (accessUserSharedPreferences.getUserAppStatus() == UserAppStatus.LOGIN) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    public void loginUsersCommerce(View view) {
        if (formSignInValid()) {
            UserLoginDTO userLogin = new UserLoginDTO()
                    .setUsername(etUserName.getText().toString())
                    .setPassword(etPassword.getText().toString());
            authenticateCallApi = administrationAssurancesApi.authenticateUser(userLogin);
            authenticateCallApi.enqueue(new Callback<AuthenticationDTO>() {
                @Override
                public void onResponse(Call<AuthenticationDTO> call, Response<AuthenticationDTO> response) {
                    AuthenticationDTO authentication = response.body();
                    if (authentication.isAuthenticate()) {

                        accessUserSharedPreferences.logInUser(authentication.getUserId());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    } else {
                        Toast.makeText(LoginActivity.this, authentication.getMessage(), Toast.LENGTH_SHORT).show();
                        clearFormSignIn();
                    }
                }

                @Override
                public void onFailure(Call<AuthenticationDTO> call, Throwable t) {

                }
            });

        } else {
            Toast.makeText(this, "Complete los campos de usuario y/o contraseña para autenticar su cuenta", Toast.LENGTH_SHORT).show();
        }

        //
    }

    private void clearFormSignIn() {
        etUserName.getText().clear();
        etPassword.getText().clear();
    }

    private boolean formSignInValid() {
        return !TextUtils.isEmpty(etUserName.getText()) && !TextUtils.isEmpty(etPassword.getText());
    }

    public void registerUsersCommerce(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Roghur Garantias");
        builder.setMessage("Proximamente estara habilitado el registro, contactate con administracion para registrar tus datos");
        builder.setPositiveButton("Aceptar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Presione nuevamente el boton atras para salir", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }
}