package com.github.sviatoslavslysh.vacationvibes.functionality.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;


import com.github.sviatoslavslysh.vacationvibes.MainActivity;
import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.cardstack.CardStackAdapter;
import com.github.sviatoslavslysh.vacationvibes.functionality.NavigationBarActivity;
import com.github.sviatoslavslysh.vacationvibes.model.AddReactionResponse;
import com.github.sviatoslavslysh.vacationvibes.model.HomeViewModel;
import com.github.sviatoslavslysh.vacationvibes.model.Place;
import com.github.sviatoslavslysh.vacationvibes.repository.PlaceRepository;
import com.github.sviatoslavslysh.vacationvibes.utils.PlaceCallback;
import com.github.sviatoslavslysh.vacationvibes.utils.PreferencesManager;
import com.github.sviatoslavslysh.vacationvibes.utils.ToastManager;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.List;

public class HomeFragment extends Fragment implements CardStackListener {
    private CardStackView cardStackView;
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    private PlaceRepository placeRepository;
    private PreferencesManager preferencesManager;
    private HomeViewModel homeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        placeRepository = ((NavigationBarActivity) requireActivity()).getPlaceRepository();
        preferencesManager = ((NavigationBarActivity) requireActivity()).getPreferencesManager();
        cardStackView = rootView.findViewById(R.id.card_stack_view);
        if (!homeViewModel.isDataLoaded()) {
            adapter = new CardStackAdapter(homeViewModel.getPlaces());
            manager = new CardStackLayoutManager(requireContext(), HomeFragment.this);
            setupCardStackView();
        } else if (!homeViewModel.isAwaitingResponse()) {
            // do nothing if already awaiting response on another (probably already hidden) activity
            loadPlaces();
        }
        return rootView;
    }


    private void setupCardStackView() {
//        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(1);
//        manager.setTranslationInterval(8.0f);
//        manager.setScaleInterval(0.95f);
//        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(45.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
//        manager.setOverlayInterpolator(new LinearInterpolator());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);

        if (cardStackView.getItemAnimator() instanceof DefaultItemAnimator) {
            DefaultItemAnimator animator = (DefaultItemAnimator) cardStackView.getItemAnimator();
            animator.setSupportsChangeAnimations(false);
        }
    }

    private void loadPlaces() {
        homeViewModel.setAwaitingResponse(true);
        placeRepository.getFeed(new PlaceCallback<List<Place>>() {
            @Override
            public void onSuccess(List<Place> places) {
                homeViewModel.setAwaitingResponse(false);
                if (isAdded()) {
                    // todo delete this toast
                    ToastManager.showToast(requireActivity(), "Retrieved places successfully!");
                    adapter = new CardStackAdapter(places);
                    manager = new CardStackLayoutManager(requireContext(), HomeFragment.this);
                    setupCardStackView();
                    homeViewModel.setPlaces(places);
                }
            }

            @Override
            public void onError(String errorMessage) {
                homeViewModel.setAwaitingResponse(false);
                if (isAdded()) {
                    ToastManager.showToast(requireActivity(), errorMessage);
                }
            }

            @Override
            public void onInvalidAuth() {
                homeViewModel.setAwaitingResponse(false);
                if (isAdded()) {
                    ToastManager.showToast(requireActivity(), "Token expired");
                    preferencesManager.removeToken();
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        requireActivity().finish();
                    }, 200);
                }
            }
        });
    }

    @Override
    public void onCardAppeared(@Nullable View view, int i) {
        System.out.println("onCardAppeared i -> " + i);
        // todo modify homeViewModel
//        List<String[]> spotsData = Arrays.asList(
////                new String[]{"Yasaka Shrine", "Kyoto", "https://images.unsplash.com/photo-1713346642924-fdda99d45870"},
////                new String[]{"Fushimi Inari Shrine", "Kyoto", "https://images.unsplash.com/photo-1542767673-ee5103fedbb1"},
//                new String[]{"Bamboo Forest", "Kyoto", "https://images.unsplash.com/photo-1531021713651-fdd4ac075ac1"},
//                new String[]{"The statue of Liberty", "New York", "https://images.unsplash.com/photo-1670821911205-00c0d9582b92"},
//                new String[]{"Empire State Building", "New York", "https://images.unsplash.com/photo-1663052721527-0d971e81d257"},
//                new String[]{"Brooklyn Bridge", "New York", "https://images.unsplash.com/photo-1585163435462-7e7796fa4b9e"},
//                new String[]{"Louvre Museum", "Paris", "https://images.unsplash.com/photo-1555929940-b435de81524e"},
//                new String[]{"Eiffel Tower", "Paris", "https://images.unsplash.com/photo-1609971757431-439cf7b4141b"},
//                new String[]{"Big Ben", "London", "https://images.unsplash.com/photo-1454793147212-9e7e57e89a4f"},
//                new String[]{"Great Wall of China", "China", "https://images.unsplash.com/photo-1558981017-9c65fb6f2530"}
//        );
//
//        Random random = new Random();
//        int randomIndex = random.nextInt(spotsData.size());
//
//        String[] selectedSpot = spotsData.get(randomIndex);
//        Place newPlace = new Place(selectedSpot[0], selectedSpot[1], selectedSpot[2]);
//        cardStackView.post(() -> adapter.addPlace(newPlace));
//        adapter.addSpot(newSpot);
//        List<Spot> spots = adapter.getSpots();
//        spots.add(newSpot);
////        adapter.setSpots(spots);
//        adapter = new CardStackAdapter(createSpots());
    }

    @Override
    public void onCardDragging(@Nullable Direction direction, float v) {
        System.out.println("onCardDragging " + direction + " " + v);
    }

    @Override
    public void onCardSwiped(@Nullable Direction direction) {
        List<Place> allPlaces = adapter.getPlaces();
        allPlaces.remove(0);
        homeViewModel.setPlaces(allPlaces);
        String reaction = "";
        assert direction != null;
        if (direction.equals(Direction.Left)) {
            // dislike
            reaction = "dislike";
        } else if (direction.equals(Direction.Right)) {
            // like
            reaction = "like";
        }
        Place currentPlace = adapter.getPlaces().get(0);
        placeRepository.addReaction(currentPlace.getId(), reaction, new PlaceCallback<AddReactionResponse>() {
            @Override
            public void onSuccess(AddReactionResponse result) {
                // reaction sent
            }

            @Override
            public void onError(String errorMessage) {
                // todo maybe add to queue of not sent request
                ToastManager.showToast(requireActivity(), errorMessage);
            }

            @Override
            public void onInvalidAuth() {
                ToastManager.showToast(requireActivity(), "Token expired");
                preferencesManager.removeToken();
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    requireActivity().finish();
                }, 200);
            }
        });
    }

    @Override
    public void onCardRewound() {
        System.out.println("onCardRewound");
    }

    @Override
    public void onCardCanceled() {
        System.out.println("onCardCanceled");
    }

    @Override
    public void onCardDisappeared(@Nullable View view, int i) {
        System.out.println("onCardDragging " + view + " " + i);
    }
}
