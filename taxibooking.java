import java.util.*;

class Booking {
    int bookingId;
    int customerId;
    char pickupPoint;
    char dropPoint;
    int pickupTime;
    int dropTime;
    int amount;

    public Booking(int bookingId, int customerId, char pickupPoint, char dropPoint, int pickupTime, int dropTime, int amount) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.pickupPoint = pickupPoint;
        this.dropPoint = dropPoint;
        this.pickupTime = pickupTime;
        this.dropTime = dropTime;
        this.amount = amount;
    }

    public String toString() {
        return bookingId + "\t" + customerId + "\t" + pickupPoint + "\t" + dropPoint + "\t" + pickupTime + "\t" + dropTime + "\t" + amount;
    }
}

class Taxi {
    int taxiId;
    int totalEarnings;
    char currentPoint;
    int freeTime;
    List<Booking> bookings;

    public Taxi(int id) {
        this.taxiId = id;
        this.totalEarnings = 0;
        this.currentPoint = 'A';
        this.freeTime = 0;
        this.bookings = new ArrayList<>();
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
        totalEarnings += booking.amount;
        currentPoint = booking.dropPoint;
        freeTime = booking.dropTime;
    }

    public void printDetails() {
        System.out.println("Taxi-" + taxiId + " Total Earnings: Rs. " + totalEarnings);
        System.out.println("BookingID\tCustomerID\tFrom\tTo\tPickupTime\tDropTime\tAmount");
        for (Booking b : bookings) {
            System.out.println(b);
        }
        System.out.println();
    }
}

class TaxiBookingSystem {
    List<Taxi> taxis;
    int bookingCounter = 1;

    public TaxiBookingSystem(int numberOfTaxis) {
        taxis = new ArrayList<>();
        for (int i = 1; i <= numberOfTaxis; i++) {
            taxis.add(new Taxi(i));
        }
    }

    private int calculateDistance(char a, char b) {
        return Math.abs(a - b) * 15;
    }

    private int calculateDropTime(char pickup, char drop, int pickupTime) {
        int timeTaken = Math.abs(drop - pickup); // 1 point = 1 hour
        return pickupTime + timeTaken;
    }

    private int calculateAmount(int distance) {
        if (distance <= 5) return 100;
        return 100 + (distance - 5) * 10;
    }

    public void bookTaxi(int customerId, char pickup, char drop, int pickupTime) {
        Taxi selectedTaxi = null;
        int minDistance = Integer.MAX_VALUE;

        for (Taxi taxi : taxis) {
            if (taxi.freeTime <= pickupTime) {
                int distance = Math.abs(taxi.currentPoint - pickup);
                if (distance < minDistance) {
                    minDistance = distance;
                    selectedTaxi = taxi;
                } else if (distance == minDistance && taxi.totalEarnings < selectedTaxi.totalEarnings) {
                    selectedTaxi = taxi;
                }
            }
        }

        if (selectedTaxi == null) {
            System.out.println("No taxi available at this time. Booking Rejected.\n");
            return;
        }

        int distance = calculateDistance(pickup, drop);
        int amount = calculateAmount(distance);
        int dropTime = calculateDropTime(pickup, drop, pickupTime);
        Booking booking = new Booking(bookingCounter++, customerId, pickup, drop, pickupTime, dropTime, amount);

        selectedTaxi.addBooking(booking);

        System.out.println("Taxi can be allotted.");
        System.out.println("Taxi-" + selectedTaxi.taxiId + " is allotted.\n");
    }

    public void displayTaxiDetails() {
        for (Taxi taxi : taxis) {
            taxi.printDetails();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TaxiBookingSystem system = new TaxiBookingSystem(4);

        while (true) {
            System.out.println("1. Book a Taxi");
            System.out.println("2. Display Taxi Details");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Customer ID: ");
                    int customerId = sc.nextInt();
                    System.out.print("Enter Pickup Point (A-F): ");
                    char pickup = sc.next().toUpperCase().charAt(0);
                    System.out.print("Enter Drop Point (A-F): ");
                    char drop = sc.next().toUpperCase().charAt(0);
                    System.out.print("Enter Pickup Time (in 24hr format): ");
                    int pickupTime = sc.nextInt();
                    if (pickup == drop) {
                        System.out.println("Pickup and drop cannot be the same.\n");
                        break;
                    }
                    system.bookTaxi(customerId, pickup, drop, pickupTime);
                    break;

                case 2:
                    system.displayTaxiDetails();
                    break;

                case 3:
                    System.out.println("Thank you for using the Call Taxi Booking System!");
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
