package com.manueldev.roghurgarantias.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manueldev.roghurgarantias.R;
/*
import com.manueldev.AssuranceDTOspadres.ServiceProposalAssuranceDTO;
import com.manueldev.AssuranceDTOspadres.models.AssuranceDTO;
*/

import com.manueldev.roghurgarantias.models.ActiveAssuranceDTO;

import java.util.ArrayList;
import java.util.List;

public class AssurancesAdapter extends RecyclerView.Adapter<AssurancesAdapter.ViewHolderTypeAssuranceDTOService> {

    private Context context;
    private List<ActiveAssuranceDTO> assurances;
    private final ItemViewClickListener itemViewClickListener;

    public AssurancesAdapter(Context context,ItemViewClickListener itemViewClickListener, List<ActiveAssuranceDTO> assurances) {
        this.context = context;
        this.itemViewClickListener = itemViewClickListener;
        this.assurances = new ArrayList<>(assurances);
        //this.sharedPreferences = context.getSharedPreferences("info_register_parents", Context.MODE_PRIVATE);

    }


    public void setAssurances(List<ActiveAssuranceDTO> assurances){
        this.assurances = assurances;
    }

    @NonNull
    @Override
    public ViewHolderTypeAssuranceDTOService onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item_assurances, parent, false);
        return new ViewHolderTypeAssuranceDTOService(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTypeAssuranceDTOService holder, int i) {

        ActiveAssuranceDTO assurance = assurances.get(i);

        holder.tvClientFullName.setText(assurance.getClientFullName());
        holder.tvProductName.setText(assurance.getProductName());
        holder.tvAssuranceState.setText(assurance.getAssuranceState());
        holder.tvDateAssuranceState.setText(assurance.getAssuranceDateState());

        holder.ratingProduct.setRating(1 + (int) (Math.random() * 4));

        holder.ratingProduct.setEnabled(false);

        Glide.with(context)
                .load(R.mipmap.barcode_product)
                .circleCrop()
                .into(holder.imgBarcode);




    }

    @Override
    public int getItemCount() {
        return assurances.size();
    }

    public class ViewHolderTypeAssuranceDTOService extends RecyclerView.ViewHolder  implements View.OnClickListener{

        ImageView imgBarcode;
        TextView tvClientFullName, tvProductName, tvAssuranceState, tvDateAssuranceState;
        RatingBar ratingProduct;

        public ViewHolderTypeAssuranceDTOService(@NonNull View itemView) {
            super(itemView);

            imgBarcode = itemView.findViewById(R.id.img_product_assurances_history);
            tvClientFullName = itemView.findViewById(R.id.client_full_name_assurances_history);
            tvProductName = itemView.findViewById(R.id.product_name_assurances_history);
            tvAssuranceState = itemView.findViewById(R.id.current_state_assurance_history);
            tvDateAssuranceState = itemView.findViewById(R.id.currant_state_date_assurance_history);

            ratingProduct = itemView.findViewById(R.id.rating_product);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    itemViewClickListener.onClickItemView(position);
                }
            });
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            itemViewClickListener.onClickItemView(position);
        }
    }

    public interface ItemViewClickListener {

        void onClickItemView(int position);


    }


}
