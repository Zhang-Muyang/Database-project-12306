import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class cmda {

    public static void main(String[] args) throws IOException {
        String resource = "db_project.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        Scanner scr = new Scanner(System.in);
        Passenger passenger = new Passenger(sqlSession);
//        passenger.searchTrain();  //用城市找火车
//        passenger.logIn();
//        passenger.reserveTicket();  //订票，需要登陆，用户名获知梦幻  密码123456
//
//        sqlSession.commit();
//        passenger.queryOrder();    //用订单号查订单
//        passenger.refundTicket();  //用订单号退票
//        sqlSession.commit();
//        sqlSession.close();

        Administrator ad = new Administrator(sqlSession);
//        ad.logIn();
//        if(ad.logIn() != null){
//
//            while (true){
//                System.out.println("Enter \"quit\" to stop\n");
//                System.out.println("1 to add a train line.");
//                System.out.println("2 to delete a train.");
//                System.out.println("3 to add a station.");
//                System.out.println("4 to delete a station.");
//                System.out.println("\nChoose:");
//
//                String Cmd = scr.next();
//                if(Cmd.equals("quit")) break;
//
//                switch (Cmd){
//                    case "1" :
//                        ad.AddTrain();
//                        sqlSession.commit();
//                        break;
//                    case "2":
//                        System.out.println("请输入删除的列车名字：");
//                        String id = scr.next();
//                        ad.DelTrain(id);
//                        sqlSession.commit();
//                        break;
//                    case "3":
//                        ad.AddStat();
//                        sqlSession.commit();
//                        break;
//                    case "4":
//                        ad.DelStat();
//                        sqlSession.commit();
//                        break;
//                }
//            }
//            ad.UpSt();
//            sqlSession.commit();
        passenger.get_train_info();


            ad.UpTr();
            sqlSession.commit();


        passenger.searchTrain();



//        }

        sqlSession.close();
    }
}
