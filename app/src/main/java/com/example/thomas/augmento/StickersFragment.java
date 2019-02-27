package com.example.thomas.augmento;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


public class StickersFragment extends Fragment {

    View parentHolder;

    private List<Sticker> stickerList=new ArrayList<>();
    private RecyclerView recyclerView;
    private StickerAdapter mAdapter;
    private ModelLoader modelLoader;

    private ImageView backArrowImageView;
    public void initialize()
    {
        backArrowImageView=parentHolder.findViewById(R.id.backArrowImageView);
        recyclerView=parentHolder.findViewById(R.id.recyclerView);
        mAdapter=new StickerAdapter(stickerList);
        FlexboxLayoutManager mLayoutManager=new FlexboxLayoutManager(getContext());
        mLayoutManager.setFlexDirection(FlexDirection.ROW);
        mLayoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        modelLoader=new ModelLoader((ModelLoader.ModelLoaderCallbacks) getContext());
    }

    public void listeners()
    {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Sticker sticker=stickerList.get(position);
                int count;

                LocalStorage storeSticker=new LocalStorage(getContext());
                LocalStorage shareSticker=new LocalStorage(getContext());

                if(shareSticker.getStorage("StickerCount").equalsIgnoreCase("0"))
                {
                    count=0;
                }
                else
                {
                    count= Integer.parseInt(shareSticker.getStorage("StickerCount"));
                }
                if(sticker.getGifUrl()==R.drawable.flyingsaucer)
                {
                    storeSticker.addStorage("StickerID",String.valueOf(R.raw.flyingsacuer));
                    modelLoader.loadModel(getContext(),R.raw.flyingsacuer);
                    Toast.makeText(getContext(),sticker.getGifUrl()+" is selected!",Toast.LENGTH_SHORT).show();

                    ++count;
                    shareSticker.addStorage("StickerCount",String.valueOf(count));
                    shareSticker.addStorage("Sticker"+count, String.valueOf(R.drawable.flyingsaucer));
                }
                else if(sticker.getGifUrl()==R.drawable.house)
                {
                    storeSticker.addStorage("StickerID",String.valueOf(R.raw.house));
                    modelLoader.loadModel(getContext(),R.raw.house);
                    Toast.makeText(getContext(),sticker.getGifUrl()+" is selected!",Toast.LENGTH_SHORT).show();

                    ++count;
                    shareSticker.addStorage("StickerCount",String.valueOf(count));
                    shareSticker.addStorage("Sticker"+count, String.valueOf(R.drawable.house));
                }
                else if(sticker.getGifUrl()==R.drawable.spaceman)
                {
                    storeSticker.addStorage("StickerID",String.valueOf(R.raw.spaceman));
                    modelLoader.loadModel(getContext(),R.raw.spaceman);
                    Toast.makeText(getContext(),sticker.getGifUrl()+" is selected!",Toast.LENGTH_SHORT).show();

                    ++count;
                    shareSticker.addStorage("StickerCount",String.valueOf(count));
                    shareSticker.addStorage("Sticker"+count, String.valueOf(R.drawable.spaceman));
                }
                else if(sticker.getGifUrl()==R.drawable.uphouse)
                {
                    storeSticker.addStorage("StickerID",String.valueOf(R.raw.uphouse));
                    modelLoader.loadModel(getContext(),R.raw.uphouse);
                    Toast.makeText(getContext(),sticker.getGifUrl()+" is selected!",Toast.LENGTH_SHORT).show();

                    ++count;
                    shareSticker.addStorage("StickerCount",String.valueOf(count));
                    shareSticker.addStorage("Sticker"+count, String.valueOf(R.drawable.uphouse));

                }
                else if(sticker.getGifUrl()==R.drawable.andy)
                {
                    storeSticker.addStorage("StickerID",String.valueOf(R.raw.andy));
                    modelLoader.loadModel(getContext(),R.raw.andy);
                    Toast.makeText(getContext(),sticker.getGifUrl()+" is selected!",Toast.LENGTH_SHORT).show();

                    ++count;
                    shareSticker.addStorage("StickerCount",String.valueOf(count));
                    shareSticker.addStorage("Sticker"+count, String.valueOf(R.drawable.andy));
                }


                getActivity().onBackPressed();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        backArrowImageView.setOnClickListener(v -> {

            getActivity().onBackPressed();

        });
    }

    public void prepareStickerData()
    {
        Sticker sticker=new Sticker(R.drawable.flyingsaucer);
        stickerList.add(sticker);

        sticker=new Sticker(R.drawable.house);
        stickerList.add(sticker);

        sticker=new Sticker(R.drawable.spaceman);
        stickerList.add(sticker);

        sticker=new Sticker(R.drawable.uphouse);
        stickerList.add(sticker);

        sticker=new Sticker(R.drawable.andy);
        stickerList.add(sticker);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentHolder= inflater.inflate(R.layout.fragment_stickers, container, false);

        initialize();
        listeners();

        prepareStickerData();

        return parentHolder;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
