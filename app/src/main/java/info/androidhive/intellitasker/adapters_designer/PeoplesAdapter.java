package info.androidhive.intellitasker.adapters_designer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.androidhive.intellitasker.R;
import info.androidhive.intellitasker.Entities.People;

public class PeoplesAdapter extends RecyclerView.Adapter<PeoplesAdapter.MyViewHolder> {

    private List<People> peopleList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, study, occupation;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_l);
            study = (TextView) view.findViewById(R.id.study_l);
            occupation = (TextView) view.findViewById(R.id.occupation_l);
        }
    }


    public PeoplesAdapter(List<People> peopleList) {
        this.peopleList = peopleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.people_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        People movie = peopleList.get(position);
        holder.name.setText(movie.getName());
        holder.study.setText(movie.getStudy());
        holder.occupation.setText(movie.getOccupation());
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }
}
