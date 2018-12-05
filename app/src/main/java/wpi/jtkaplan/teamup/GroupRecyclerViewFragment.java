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

import wpi.jtkaplan.teamup.model.Professor;
import wpi.jtkaplan.teamup.model.Student;
import wpi.jtkaplan.teamup.model.User;

public class GroupRecyclerViewFragment
        <UserType extends User> // A group view may want to include a professor as well as the students. likewise, we may have different types of users.
        extends Fragment { // TODO : use members instead of students??

    private List<User> members;
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
        members = new ArrayList<User>();
        members.add(new Professor("DIFFERENT MODEL", "x", "x"));
        members.add(new Student("Harvey Milk", "21", "im@straight.com"));
        members.add(new Student("Frida Kahlo", "32", "nothurt@faithful.com"));
        members.add(new Student("Edward Culkin", "8", "wheredid@gowrong.com"));
        members.add(new Student("John Adams", "15", "emaildoesnt@exist.yet"));
        members.add(new Student("Abraham Lincoln", "209", "dont@shootme.bro"));
    }

    private void initializeAdapter() {
        GroupRVAdapter adapter = new GroupRVAdapter(members);
        adapter.getItemId(members.size() - 1);
        rv.setAdapter(adapter);
    }
}