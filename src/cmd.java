import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class cmd {
    public static void main(String[] args) throws IOException {
        String resource = "db_project.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();

        Passenger passenger = new Passenger(sqlSession);
        //double start = System.currentTimeMillis();
        passenger.searchTrain();  //用城市找火车
        //double end = System.currentTimeMillis();
//       passenger.check_Informatioin_after_search_train(); //余票
        passenger.logIn();

//passenger.register();
//sqlSession.commit();
//

       passenger.reserveTicket();  //订票，需要登陆，用户名获知梦幻  密码123456
        sqlSession.commit();
//       passenger.queryOrder();    //用订单号查订单
        passenger.queryPassengerEveryOrder();
//        passenger.refundTicket();  //用订单号退票

//        sqlSession.commit();
        sqlSession.close();
    }
}
