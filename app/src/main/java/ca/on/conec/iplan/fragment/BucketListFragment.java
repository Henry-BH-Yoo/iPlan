/**
 * FileName : BucketListFragment.java
 * Revision History :
 *          2021 03 25  Henry   Create
 *          2021 04 08  Henry    modify
 *          2021 04 19  Henry    complete bucket
 *          2021 04 22  Henry    modify
 *          2021 04 23  Henry    modify
 */

package ca.on.conec.iplan.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ca.on.conec.iplan.R;
import ca.on.conec.iplan.adapter.BucketListAdapter;
import ca.on.conec.iplan.adapter.ItemTouchHelperCallback;
import ca.on.conec.iplan.adapter.ItemTouchHelperListener;
import ca.on.conec.iplan.entity.BucketList;
import ca.on.conec.iplan.viewmodel.BucketListViewModel;


public class BucketListFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<BucketList> bucketList = new ArrayList<BucketList>();
    private BucketListAdapter bucketListAdapter;

    private ItemTouchHelper mItemTouchHelper;
    private BottomSheetBucketFragment bottomSheetBucketFragment;

    private BucketListViewModel bucketListViewModel;

    public BucketListFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_bucket_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(v.getContext(), 1));


        bucketListViewModel = new ViewModelProvider.AndroidViewModelFactory(
                getActivity().getApplication()).create(BucketListViewModel.class);

        bottomSheetBucketFragment = new BottomSheetBucketFragment();

        CoordinatorLayout frameLayout = v.findViewById(R.id.bottomSheetBucket);
        BottomSheetBehavior<CoordinatorLayout> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        FloatingActionButton addBucketBtn = v.findViewById(R.id.addBucket);

        addBucketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(0);
            }
        });

        getDataList();


        return v;
    }

    public void showBottomSheetDialog(int bucketId) {
        Bundle bundle = new Bundle();
        bundle.putInt("bucketId" , bucketId);

        getChildFragmentManager().beginTransaction().add(bottomSheetBucketFragment, bottomSheetBucketFragment.getTag());
        bottomSheetBucketFragment.setArguments(bundle);
        bottomSheetBucketFragment.show(getParentFragmentManager(), bottomSheetBucketFragment.getTag());
    }



    private void getDataList() {

        bucketListViewModel.findAll().observe(getViewLifecycleOwner(),  bucketList -> {
            this.bucketList = bucketList;
            bindAdapter();
        });
    }


    /**
     * binding recyclerView.
     */
    private void bindAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        bucketListAdapter = new BucketListAdapter(getActivity().getApplicationContext());
        recyclerView.setAdapter(bucketListAdapter);

//        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(bucketListAdapter));
//        mItemTouchHelper.attachToRecyclerView(recyclerView);

        bucketListAdapter.setBucketList(bucketList);

        bucketListAdapter.setOnItemClickListener(new BucketListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int bucketId) {
                showBottomSheetDialog(bucketId);
            }

            @Override
            public void onDeleteBtnClick(View v, BucketList bucket) {
                BucketListViewModel.delete(bucket);
            }
        });

    }

}