import database.*
import database.UserDynamicSqlSupport.User.age
import database.UserDynamicSqlSupport.User.id
import database.UserDynamicSqlSupport.User.name
import database.UserDynamicSqlSupport.User.profile
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.mybatis.dynamic.sql.SqlBuilder.isEqualTo
import org.mybatis.dynamic.sql.SqlBuilder.isGreaterThanOrEqualTo

fun main(args: Array<String>) {
    list5_4_22()
}

//リスト5.4.2
fun createSessionFactory(): SqlSessionFactory {
    val resource = "mybatis-config.xml"
    val inputStream = Resources.getResourceAsStream(resource)
    return SqlSessionFactoryBuilder().build(inputStream)
}

//Selectの実装方法
fun list5_4_3(){
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val user = mapper.selectByPrimaryKey(100)
        println(user)
    }
}

//Where句での検索
fun list5_4_4(){
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val userList = mapper.select {
            where(name, isEqualTo("Jiro"))
        }
        println(userList)
    }
}

//count
fun list5_4_6(){
    createSessionFactory().openSession().use{ session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.count {
            where(age, isGreaterThanOrEqualTo(25))
        }
        println(count)
    }
}

//全レコードを対象とする
fun list5_4_9(){
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.count { allRows() }
        println(count)
    }
}

//Insertの実装方法
fun list5_4_10(){
    val user = UserRecord(103, "Shiro", 18, "Hello")
    createSessionFactory().openSession().use{ session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.insert(user)
        session.commit()
        println("${count}行のレコードを挿入しました。")
    }
}

//複数レコードのInsert
fun list5_4_12(){
    val userList = listOf(UserRecord(104, "Goro", 15, "Hello"), UserRecord(105, "Rokuro", 13, "Hello"))
    createSessionFactory().openSession().use{ session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.insertMultiple(userList)
        session.commit()
        println("${count}行のレコードを挿入しました")
    }
}

//Updateの実装
fun list5_4_14(){
    val user = UserRecord(id = 105, profile = "Bye")
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.updateByPrimaryKeySelective(user)
        session.commit()
        println("${count}行のレコードを更新しました")
    }
}

//主キー以外の検索条件で更新
fun list5_4_16(){
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.update {
            set(profile).equalTo("Hey")
            where(id, isEqualTo(104))
        }
        session.commit()
        println("${count}行のレコードを更新しました")
    }
}

//Deleteの実装
fun list5_4_20(){
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.deleteByPrimaryKey(102)
        session.commit()
        println("${count}行のレコードを削除しました")
    }
}

//主キー以外の検索条件でDELETE
fun list5_4_22(){
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.delete {
            where(name, isEqualTo("Jiro"))
        }
        session.commit()
        println("${count}行のレコードを削除しました")
    }
}