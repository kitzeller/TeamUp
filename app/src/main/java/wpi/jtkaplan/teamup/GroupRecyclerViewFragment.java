package wpi.jtkaplan.teamup;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import wpi.jtkaplan.teamup.model.Member;

public class GroupRecyclerViewFragment extends Fragment {

    private List<Member> members;
    private RecyclerView rv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        rv = view.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        return view;
    }

    private void initializeData() {
        // TODO: Need to get this data from Google Cloud for each member of the current group.
        members = new ArrayList<Member>();
        members.add(new Member("Harvey Milk", "21", "im@straight.com"));
        members.add(new Member("Frida Kahlo", "32", "nothurt@faithful.com"));
        members.add(new Member("Edward Culkin", "8", "wheredid@gowrong.com"));
        members.add(new Member("John Adams", "15", "emaildoesnt@exist.yet"));
        members.add(new Member("Abraham Lincoln", "209", "dont@shootme.bro"));
    }

    private void initializeAdapter() {
        GroupRVAdapter adapter = new GroupRVAdapter(members);
        adapter.getItemId(members.size() - 1);
        rv.setAdapter(adapter);
    }
}