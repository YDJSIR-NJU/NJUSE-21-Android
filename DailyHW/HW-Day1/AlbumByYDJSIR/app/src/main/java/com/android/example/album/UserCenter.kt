package com.android.example.album

/**
 * 在内存中记录用户名和密码，仅做演示，实际项目需要持久化存储
 *
 * @author YDJSIR
 */
object UserCenter {

    // user email -> user obj
    private val users = mutableMapOf<String, User>()

    /**
     * 当前在线用户
     */
    var signInUser: User? = null

    fun addUser(user: User) {
        if(!users.containsKey(user.email)) { // 没有这个邮箱对应用户才能注册
            users[user.email] = user
        }
    }

    /**
     * 返回用户对象，如果该用户不存在返回空
     */
    fun getUserByEmail(email: String): User? {
        var reUser = users[email]
//        if (reUser != null) {
//            reUser.password = ""
//        } // 返回用户时不能直接给密码
        return reUser
    }

    fun validatePassword(email: String, password: String): Boolean {
        var reUser = users[email]
        if(reUser!=null){
            return reUser.password == password
        }
        else{
            return false
        }
    }

}