package com.manueldev.roghurgarantias.apis;


import com.manueldev.roghurgarantias.models.ActiveAssuranceDTO;
import com.manueldev.roghurgarantias.models.AssuranceDTO;
import com.manueldev.roghurgarantias.models.AuthenticationDTO;
import com.manueldev.roghurgarantias.models.ClaimAssuranceDTO;
import com.manueldev.roghurgarantias.models.ClientDTO;
import com.manueldev.roghurgarantias.models.ProductDTO;
import com.manueldev.roghurgarantias.models.ProfileUserCommerceDTO;
import com.manueldev.roghurgarantias.models.UserLoginDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdministrationAssuranceApi {

    @GET("assurances/all")
    Call<List<ActiveAssuranceDTO>> getAllAssurancesHistory();

    @POST("clients")
    Call<ClientDTO> registerClient(@Body ClientDTO client);


    @POST("assurances")
    Call<AssuranceDTO> activeAssurance(@Body AssuranceDTO assurance);


    @GET("clients/search/{clientDni}")
    Call<ClientDTO> searchClientByDni(@Path("clientDni") String clientDni);

    @GET("assurances/search/client/{clientId}")
    Call<List<ActiveAssuranceDTO>> getAllAssurancesByClientId(@Path("clientId") long clientId);

    @GET("product/search/")
    Call<ProductDTO> getProductByCodeOrSerial(@Query("productCode") String productCode, @Query("productSerial") String productSerial);

    @GET("usersCommerce/profile/{usersCommerceId}")
    Call<ProfileUserCommerceDTO> getProfileUserCommerce(@Path("usersCommerceId") long usersCommerceId);

    @POST("assurances/claim")
    Call<AssuranceDTO> claimAssurance(@Body ClaimAssuranceDTO claimAssurance);

    @POST("admin/login")
    Call<AuthenticationDTO> authenticateUser(@Body UserLoginDTO userLogin);

    /*@POST("parents")
    Call<Parents> registerParents(@Body Parents parents);

    @POST("session/authenticate/{userType}")
    Call<AuthenticateParents> authenticateParents(@Path("userType") int userType, @Body UserParents userParents);

    @GET("typeNanny")
    Call<TypeNannyForService> getAllTypesNannys(@Query("entitiesInclude") boolean questionsInclude);

    @POST("serviceProposal")
    Call<ResponseMessage> registerServiceProposalFromParents(@Body ServiceProposal serviceProposal);*/


}
