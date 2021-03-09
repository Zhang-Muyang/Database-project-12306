import org.apache.ibatis.session.SqlSession;


import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Passenger {
    Integer user_id;
    String user_name;
    String user_password;
    String phone_num;

    String id_card_num;
    List<Order> orderList;
    Scanner scr = new Scanner(System.in);
    private Passengerinterface passengerinterface;

    private SqlSession sqlSession;

    public Passenger(SqlSession sqlSession) {
        passengerinterface = sqlSession.getMapper(Passengerinterface.class);
        this.sqlSession = sqlSession;
    }

    public Passenger() {
    }


    public void searchTrain() {
        System.out.println("开始进行搜索");
        System.out.println("请输入出发城市");
        String depart_city = scr.next();
        System.out.println("请输入到达城市");
        String arrive_city = scr.next();
        List<Ticket> res = this.passengerinterface.searchTrain(depart_city, arrive_city);
        if (res.size() != 0) {
            System.out.println("正在查询中，请稍后");
            List<Line> depart = new ArrayList<>();
            List<Line> arrive = new ArrayList<>();
            boolean print = false;
//        Line temp;
//
            for (int i = 0; i < res.size(); i++) {
                res.get(i).arrive_stop = passengerinterface.queryLineId(res.get(i).arrive_stop.stop_id);
                res.get(i).depart_stop = passengerinterface.queryLineId(res.get(i).depart_stop.stop_id);
            }


            for (int i = 0; i < res.size(); i++) {
                System.out.print("车次：" + res.get(i).arrive_stop.train_num + "  出发站：" + res.get(i).depart_stop.stationCity.station + "  到达站：" + res.get(i).arrive_stop.stationCity.station);
                System.out.println(" 出发时间：" + res.get(i).depart_stop.depart_time + " 到达时间：" + res.get(i).arrive_stop.arrive_time);
            }
        } else {
            System.out.println("没有直达线路，正在为您搜索一次换乘");
            PriorityQueue<TicketPair> heap = new PriorityQueue<>(20, new Comparator<TicketPair>() {
                @Override
                public int compare(TicketPair f, TicketPair t) {

                    int t1 = get_time(f.startTicket, f.arriveTicket);

                    int t2 = get_time(t.startTicket, t.arriveTicket);
                    return t1 - t2;
                }
            });
            List<StationCity> focal_city = passengerinterface.focal_point(depart_city, arrive_city);

            for (StationCity stationCity : focal_city) {
                List<Ticket> from = this.passengerinterface.searchTrain(depart_city, stationCity.city);
                List<Ticket> to = this.passengerinterface.searchTrain(stationCity.city, arrive_city);

                for (int i = 0; i < from.size(); i++) {
                    from.get(i).arrive_stop = passengerinterface.queryLineId(from.get(i).arrive_stop.stop_id);
                    from.get(i).depart_stop = passengerinterface.queryLineId(from.get(i).depart_stop.stop_id);
                }
                for (int i = 0; i < to.size(); i++) {
                    to.get(i).arrive_stop = passengerinterface.queryLineId(to.get(i).arrive_stop.stop_id);
                    to.get(i).depart_stop = passengerinterface.queryLineId(to.get(i).depart_stop.stop_id);
                }

                if (heap.size() > 20) {
                    TicketPair best = heap.peek();
                    int max = Integer.MAX_VALUE;
                    for (Ticket f : from) {
                        if (get_one_time(f) < max) {
                            max = get_one_time(f);
                        }
                    }
                    int max2 = Integer.MAX_VALUE;
                    for (Ticket t : to) {
                        if (get_one_time(t) < max2) {
                            max2 = get_one_time(t);
                        }
                    }

                    int tempcost1 = max + max2;
                    int tempcost2 = get_time(best.startTicket, best.arriveTicket);
                    if (((double) tempcost1 / tempcost2) >= 1) continue;
                }

                for (Ticket f : from) {
                    for (Ticket t : to) {
                        heap.add(new TicketPair(f, t));
                    }
                }
            }
            int cnt = 0;
            System.out.println(heap.size());
            while (!heap.isEmpty()) {
                cnt++;
                if (cnt == 20) break;
                TicketPair t = heap.poll();
                System.out.println(t.startTicket.depart_stop.train_num + " " + t.startTicket.depart_stop.stationCity.station + " " +
                        t.startTicket.arrive_stop.stationCity.station + " " + t.startTicket.depart_stop.depart_time + " "
                        + t.startTicket.arrive_stop.arrive_time + " " +

                        t.arriveTicket.depart_stop.train_num + " " + t.arriveTicket.depart_stop.stationCity.station + " " +
                        t.arriveTicket.arrive_stop.stationCity.station + " " + t.arriveTicket.depart_stop.depart_time + " " + t.arriveTicket.arrive_stop.arrive_time
                );
            }
        }
    }

    public Passenger logIn() {
        System.out.println("请您登陆");
        System.out.println("请输入您的用户名");
        String user_name = scr.next();
        System.out.println("请输入您的密码");
        String user_password = scr.next();
        Passenger passenger = passengerinterface.checkUser(user_name, user_password);
        if (passenger == null) System.out.println("抱歉，您的用户名或密码输入错误");
        else {
            this.user_name = passenger.user_name;
            this.user_password = passenger.user_password;
            this.user_id = passenger.user_id;
            System.out.println("登陆成功");
        }
        return passenger;
    }

    public void register() {
        System.out.println("请输入用户名：");
        String user_name = scr.next();
        Passenger temp = passengerinterface.user_existence(-1, user_name);
        if (temp == null) {
            Passenger t = new Passenger();
            t.user_name = user_name;
            System.out.println("请输入密码：");
            String user_password = scr.next();
            t.user_password = user_password;
            passengerinterface.register(t);
            System.out.println("您的用户id为：" + t.user_id);
        } else System.out.println("抱歉该用户名已被注册");
    }

    public void check_Informatioin_after_search_train() {
        System.out.println("请输入列车车次");
        String train_num = scr.next();
        System.out.println("请输入出发车站");
        String depart_station = scr.next();
        System.out.println("请输入到达车站");
        String arrive_station = scr.next();
        System.out.println("请输入查询日期");
        String time = scr.next();
        int start_stop = passengerinterface.getStopId(train_num, depart_station);
        int arrive_stop = passengerinterface.getStopId(train_num, arrive_station);
        Remain remain = passengerinterface.queryRemain(start_stop, arrive_stop, time);
        int distance = passengerinterface.get_distance_by_station(start_stop, arrive_stop);
        int t1p = passengerinterface.get_price(distance, train_num, 1);
        int t2p = passengerinterface.get_price(distance, train_num, 2);
        int t3p = passengerinterface.get_price(distance, train_num, 3);

//        if(remain.ticket_rank1 > 0)
//        System.out.println("一号座位： 剩余票 " + "有" + "  票价为：" + t1p + "元");
//        else System.out.println("一号座位： 剩余票 " + "有" + "  票价为：" + t1p+ "元");
//        if(remain.ticket_rank2 > 0)
//            System.out.println("二号座位： 剩余票 " + "有" + "  票价为：" + t2p+ "元");
//        else System.out.println("二号座位： 剩余票 " + "有" + "  票价为：" + t2p+ "元");
//        if(remain.ticket_rank2 > 0)
//            System.out.println("三号座位： 剩余票 " + "有" + "  票价为：" + t3p+ "元");
//        else System.out.println("三号座位： 剩余票 " + "有" + "  票价为：" + t3p+ "元");

        System.out.println("一号座位： 剩余票 " + remain.ticket_rank1 + "  票价为：" + t1p + "元");
        System.out.println("二号座位： 剩余票 " + remain.ticket_rank2 + "  票价为：" + t2p + "元");
        System.out.println("三号座位： 剩余票 " + remain.ticket_rank3 + "  票价为：" + t3p + "元");
    }

    public void get_train_info(){
        System.out.println("请输入要查询的火车号：");
        String t = scr.next();
        List<Line> res = passengerinterface.get_train_info(t);
        for (int i = 0; i < res.size(); i ++){
            if(i == 0 )System.out.println("第"+(i+1) +"站为："+res.get(i).stationCity.station+" 出发时间为："+res.get(i).depart_time);
            else if(i == res.size() - 1)System.out.println("第"+(i+1) +"站为："+res.get(i).stationCity.station+" 到达时间为："+res.get(i).arrive_time);
            else
            System.out.println("第"+(i+1) +"站为："+res.get(i).stationCity.station+" 出发时间为："+res.get(i).depart_time+" 到达时间为："+res.get(i).arrive_time);
        }
    }

    public void reserveTicket() {
        System.out.println("购买车票");
        System.out.println("请输入列车车次");
        String train_num = scr.next();
        System.out.println("请输入出发车站");
        String arrive_station = scr.next();
        System.out.println("请输入到达车站");
        String depart_station = scr.next();
        System.out.println("请输入日期");
        String gotime = scr.next();
        System.out.println("请输入座位等级");
        int seat_type = scr.nextInt();

        int depart_id = passengerinterface.getStopId(train_num, arrive_station);
        int arrive_id = passengerinterface.getStopId(train_num, depart_station);
        System.out.println("您想购买几张火车票：");
        int time = scr.nextInt();
        while (time-- > 0) {
            System.out.println("请输入用户id：");
            int userid = scr.nextInt();
            Passenger passenger1 = passengerinterface.user_existence(userid,"");
            if (passenger1 == null) {
                System.out.println("用户身份信息输入错误");
            } else {
                Ticket ticket = new Ticket(depart_id, arrive_id, seat_type, gotime, userid);
                Remain remain = passengerinterface.queryRemain(depart_id, arrive_id, gotime);
                if((seat_type == 1&&remain.ticket_rank1==0) ||(seat_type == 2&&remain.ticket_rank2==0) ||(seat_type == 3&&remain.ticket_rank3==0) ){
                    System.out.println("抱歉，票已售空");
                    break;
                }

                passengerinterface.buyTicketUpdate(train_num, arrive_station, depart_station, gotime, seat_type);
                System.out.println("正在为您生成订单，请稍后");
                passengerinterface.insertTicket(ticket);
                passengerinterface.update_seat_reserve(ticket);
                Order order = new Order();
                order.ticket = ticket;
                order.passenger = new Passenger();
                order.passenger.user_id = this.user_id;
                passengerinterface.generateOrder(order);
                System.out.println("订票完成！您的订单号为: " + order.order_id);
                sqlSession.commit();
            }
        }
    }

    public void refundTicket() {
        System.out.println("退票");
        System.out.println("请输入您的订单号");
        Order order = passengerinterface.queryOrderId(scr.nextInt());
        order.ticket = passengerinterface.queryTicketId(order.ticket.ticket_id);
        passengerinterface.refundTicket(order.ticket.seat_type, order.ticket.depart_stop.stop_id, order.ticket.arrive_stop.stop_id, order.ticket.go_time);
        passengerinterface.update_seat_refund(order.ticket);
        passengerinterface.deleteOrder(order);
        passengerinterface.deleteTicket(order.ticket);

        System.out.println("退票成功");
    }

    public void queryOrder() {
        System.out.println("查询订单");
        System.out.println("请输入您的订单号");
        int order_id = scr.nextInt();
        Order order = passengerinterface.queryOrderId(order_id);
        Ticket ticket = passengerinterface.queryTicketId(order.ticket.ticket_id);
        Line depart = passengerinterface.queryLineId(ticket.depart_stop.stop_id);
        Line arrive = passengerinterface.queryLineId(ticket.arrive_stop.stop_id);
        System.out.println("您的票号为：" + ticket.ticket_id + "\n座位号为：" + ticket.seat_num + " \n行程日期为:" + ticket.go_time);
        System.out.println("出发车站为：" + depart.stationCity.station + " 出发时间为：" + depart.depart_time + " 到达车站为：" + arrive.stationCity.station
                + " 到达时间为：" + arrive.arrive_time);
        System.out.println("乘车人为：" + ticket.passenger.user_name + " 票价是：" + ticket.money + " RMB");
    }

    public void queryPassengerEveryOrder() {
        List<Order> store = passengerinterface.get_everyOrder(this.user_id);
        for (Order order : store) {
            order = passengerinterface.queryOrderId(order.order_id);
            Ticket ticket = passengerinterface.queryTicketId(order.ticket.ticket_id);
            Line depart = passengerinterface.queryLineId(ticket.depart_stop.stop_id);
            Line arrive = passengerinterface.queryLineId(ticket.arrive_stop.stop_id);
            System.out.println("您的票号为：" + ticket.ticket_id + "\n座位号为：" + ticket.seat_num + " \n行程日期为:" + ticket.go_time);
            System.out.println("出发车站为：" + depart.stationCity.station + " 出发时间为：" + depart.depart_time + " 到达车站为：" + arrive.stationCity.station
                    + " 到达时间为：" + arrive.arrive_time);
            System.out.println("乘车人为：" + ticket.passenger.user_name + " 票价是：" + ticket.money + " RMB");
            System.out.print("订单状态为：");
            if (order.existence == 'N') System.out.println("已退票");
            else System.out.println("订票成功");
            System.out.println();
        }

    }

    private int get_time(Ticket t, Ticket s) {
        int h = Integer.parseInt(t.depart_stop.depart_time.substring(0, 2));
        int m = Integer.parseInt(t.depart_stop.depart_time.substring(3, 5));
        int c = t.arrive_stop.cost_days - t.depart_stop.cost_days;
        int h1 = Integer.parseInt(t.arrive_stop.arrive_time.substring(0, 2));
        int m1 = Integer.parseInt(t.arrive_stop.arrive_time.substring(3, 5));
        int q1 = h1 + c * 24;
        int cost1 = (q1 - h) * 60 + m1 - m;
        int h2 = Integer.parseInt(s.depart_stop.depart_time.substring(0, 2));
        int m2 = Integer.parseInt(s.depart_stop.depart_time.substring(3, 5));
        int c2 = s.arrive_stop.cost_days - s.depart_stop.cost_days;
        int h12 = Integer.parseInt(s.arrive_stop.arrive_time.substring(0, 2));
        int m12 = Integer.parseInt(s.arrive_stop.arrive_time.substring(3, 5));
        int q12 = h12 + c2 * 24;
        int cost12 = (q12 - h2) * 60 + m12 - m2;
        if (h2 * 24 + m2 - h1 * 24 - m1 > 0) return cost1 + cost12 + (h2 - h1) * 60 + (m2 - m1);
        else return cost1 + cost12 + (h2 + 24 - h1) * 60 + (m2 - m1);
    }

    private int get_one_time(Ticket t) {
        int h = Integer.parseInt(t.depart_stop.depart_time.substring(0, 2));
        int m = Integer.parseInt(t.depart_stop.depart_time.substring(3, 5));
        int c = t.arrive_stop.cost_days - t.depart_stop.cost_days;
        int h1 = Integer.parseInt(t.arrive_stop.arrive_time.substring(0, 2));
        int m1 = Integer.parseInt(t.arrive_stop.arrive_time.substring(3, 5));
        int q1 = h1 + c * 24;
        int cost1 = (q1 - h) * 60 + m1 - m;
        return cost1;
    }

}


class TicketPair {
    Ticket startTicket;
    Ticket arriveTicket;

    TicketPair(Ticket a, Ticket b) {
        startTicket = a;
        arriveTicket = b;
    }
}
