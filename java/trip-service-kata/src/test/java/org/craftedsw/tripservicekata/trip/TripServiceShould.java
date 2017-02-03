package org.craftedsw.tripservicekata.trip;

import com.sun.tools.corba.se.idl.constExpr.Equal;
import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class TripServiceShould {
    public static final User NOT_LOGGED_USER = null;
    public static final User ANY_USER = new User();
    public static final User LOGGED_USER = new User();
    public static final User USER_WITH_FRIENDS_WITH_ME = new User();
    public static final User USER_WITH_FRIENDS_WITHOUT_ME = new User();
    private TripService tripService;

    @Test(expected = UserNotLoggedInException.class)
    public void throw_exception_when_user_not_logged() {
        tripService = new TripServiceClassTest(NOT_LOGGED_USER);

        tripService.getTripsByUser(ANY_USER);
    }

    @Test
    public void return_no_trips_for_users_without_friends() {
        tripService = new TripServiceClassTest(LOGGED_USER);

        List<Trip> trips = tripService.getTripsByUser(ANY_USER);

        Assert.assertThat(trips.isEmpty(), is(true));
    }

    @Test
    public void return_no_trips_for_non_friendship_users() {
        tripService = new TripServiceClassTest(LOGGED_USER);

        List<Trip> trips = tripService.getTripsByUser(userWithFriendsWithoutFriendship());

        Assert.assertThat(trips.isEmpty(), is(true));
    }

    private User userWithFriendsWithoutFriendship() {
        USER_WITH_FRIENDS_WITHOUT_ME.addFriend(ANY_USER);

        return USER_WITH_FRIENDS_WITHOUT_ME;
    }

    @Test
    public void return_trips_for_friendship_users() {
        List<Trip> userTrips = new ArrayList<Trip>();
        userTrips.add(new Trip());

        tripService = new TripServiceClassTest(LOGGED_USER, userTrips);

        List<Trip> trips = tripService.getTripsByUser(userWithFriendsWithFriendship());

        Assert.assertThat(trips, is(userTrips));
    }

    private User userWithFriendsWithFriendship() {
        USER_WITH_FRIENDS_WITH_ME.addFriend(LOGGED_USER);

        return USER_WITH_FRIENDS_WITH_ME;
    }

    private class TripServiceClassTest extends TripService {

        private User user;
        private List<Trip> trips;

        public TripServiceClassTest(User user) {
            this(user, new ArrayList<Trip>());
        }

        public TripServiceClassTest(User user, List<Trip> trips) {
            this.user = user;
            this.trips = trips;
        }

        @Override
        protected User getLoggedUser() {
            return user;
        }

        @Override
        public List<Trip> getTrips(User user) {
            return trips;
        }
    }
}

