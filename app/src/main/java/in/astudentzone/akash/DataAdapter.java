package in.astudentzone.akash;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private Context ctx;
    private int res;
    private ArrayList<ModelEntry> list;

    public DataAdapter(Context ctx, int res, ArrayList<ModelEntry> list) {
        this.ctx = ctx;
        this.res = res;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(res,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        ModelEntry entry = list.get(position);
        holder.propertyName.setText(entry.getPropertyName());
        holder.locationName.setText(entry.getCityName());
        holder.localityName.setText(entry.getLocalityName());
        holder.ownerNumber.setText(entry.getMobileNumber());
        holder.ownerName.setText(entry.getOwnerName());
        holder.preferredLanguage.setText(entry.getPreferredLanguage());
        holder.applicationStatus.setText(entry.getApplicationStatus());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, ViewProperty.class);
                intent.putExtra("property_name", holder.propertyName.getText().toString());
                intent.putExtra("location_name", holder.locationName.getText().toString());
                intent.putExtra("locality_name", holder.localityName.getText().toString());
                intent.putExtra("owner_number", holder.ownerNumber.getText().toString());
                intent.putExtra("owner_name", holder.ownerName.getText().toString());
                intent.putExtra("preferred_language", holder.preferredLanguage.getText().toString());
                intent.putExtra("application_status", holder.applicationStatus.getText().toString());
                ctx.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView propertyName, preferredLanguage, locationName, localityName, ownerNumber, ownerName, applicationStatus;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            propertyName = itemView.findViewById(R.id.property_name_txt);
            preferredLanguage = itemView.findViewById(R.id.language_txt);
            locationName = itemView.findViewById(R.id.location_name_txt);
            localityName = itemView.findViewById(R.id.locality_txt);
            ownerNumber = itemView.findViewById(R.id.owner_number_txt);
            ownerName = itemView.findViewById(R.id.owner_name_txt);
            applicationStatus = itemView.findViewById(R.id.application_status_txt);
            cardView = itemView.findViewById(R.id.data_card_property);

        }
    }
}
