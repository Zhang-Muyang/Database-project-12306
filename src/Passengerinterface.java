import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Passengerinterface {

    public int buyTicketUpdate(String train_num, String depart_station, String arrive_station, String date,@Param("seat_type") int seat_type);

    public List<Ticket> searchTrain(String depart_city, String arrive_city);  //其实是search line，后期要改

    public Passenger checkUser(String user_name, String password);



    public int generateOrder(Order order);

    public int getStopId(String train_num, String Station);

    public int insertTicket(Ticket ticket);

    public Order queryOrderId(int order_id);

    public Ticket queryTicketId(int ticket_id);

    public Remain queryRemain(int depart_stop, int arrive_stop, String time);
    public int get_price(int miles, String train_num, int seat_type);

    public int get_distance_by_station(int depart_stop, int arrive_stop);

    public List<Ticket> ticket_before_buy(int depart_stop, int arrive_stop);
    public int update_seat_reserve(Ticket ticket);
    public int update_seat_refund(Ticket ticket);
    public List<Order> get_everyOrder(int user_id);

    public Line queryLineId(int line_id);

    public List<StationCity> focal_point(String depart_city, String arrive_city);

    public int get_distance(String depart_city, String arrive_city);

    public int refundTicket(@Param("seat_type")int seat_type, int start, int end, String time);

    public int deleteOrder(Order order);

    public int deleteTicket(Ticket ticket);

    public Passenger user_existence(int user_id, String user_name);
    public int register(Passenger passenger);
    public List<Line> get_train_info(String train_num);

}
