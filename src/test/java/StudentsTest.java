import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TestName;

import java.util.Date;
import java.util.List;


public class StudentsTest {

    //region 我高兴我乐意
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @Before
    public void init() {
        //创建配置对象
        Configuration config = new Configuration().configure();
       //创建SessionFactory, 生成Session
        sessionFactory = config.buildSessionFactory();
        //生成session
        session = sessionFactory.getCurrentSession();
        //开始事务
        transaction = session.beginTransaction();
    }

    @After
    public void destory() {
        //transaction.commit();
        session.close();
        sessionFactory.close();
    }
    //endregion

    @Test
    public void testSaveStudents() {
        //创建进行持久化对象
        Students student = new Students(3, "赵六6", "male", new Date(), "厦门市");

        //session.save(student);
        //session.persist(student);不存在的id才可以保存该对象
        //session.delete(student);
        //session.update(student);
        //session.saveOrUpdate(student); 更新与保存均根据id来判断。若不存在，则插入，反之亦然

        Students student_get = session.load(Students.class,2);
        System.out.println(student_get.toString());
    }

    /**
     * 测试SQL执行顺序
     */
    @Test
    public void test_01() {
        //创建进行持久化对象

        Students student_delete = new Students();
        student_delete.setSid(4);
        session.delete(student_delete);

        session.flush();

        Students student_insert = new Students(3, "赵六6", "male", new Date(), "厦门市");
        session.save(student_insert);

        Students students_update = new Students(2, "小黄2", "female", new Date(), "厦门市");
        session.update(students_update);

        //session.getTransaction().rollback();
    }

    /**
     * SQL查询
     */
    @Test
    public void Test_query() {
        String sql = "select * from students where sname like :tname";
        List<Students> studentsList = session.createSQLQuery(sql).setParameter("tname","%n%").addEntity(Students.class).list();
        for (int i = 0; i < studentsList.size(); i++) {
            System.out.println(studentsList.get(i));
        }
    }

    /**
     * HQL查询
     */
    @Test
    public void Test_query_02() {
        String hql = "from Students order by sid desc ";
        List<Students> studentsList = session.createQuery(hql).list();
        for (int i = 0; i < studentsList.size(); i++) {
            System.out.println(studentsList.get(i));
        }
    }

    /**
     * QBC查询
     */
    @Test
    public void test_query_03() {
        List<Students> studentsList = session.createCriteria(Students.class).addOrder(Order.desc("sid")).list();
        for (Students students : studentsList) {
            System.out.println(students);
        }
    }
}
