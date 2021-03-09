import java.util.Date;

public class Ticket {
    int ticket_id;
    Line depart_stop;
    Line arrive_stop;
    int ticket_entrance;
    int seat_type;
    String seat_num;
    int money;
    double ticket_price;
    String go_time;
    char existence;
    Passenger passenger;

    Ticket( int depart_stop_id, int arrive_stop_id, int seat_type, String go_time, int user_id){
        this.depart_stop = new Line();
        this.arrive_stop = new Line();
        this.depart_stop.stop_id = depart_stop_id;
        this.arrive_stop.stop_id = arrive_stop_id;
        this.seat_type = seat_type;
        this.go_time = go_time;
        this.passenger = new Passenger();
        this.passenger.user_id = user_id;
    }

    Ticket(){

    }

    public void print(){

    }


}
