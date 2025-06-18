package com.example.ckc_englihoo.Utils

import android.content.Context
import android.content.SharedPreferences
import com.example.ckc_englihoo.DataClass.Student
import com.example.ckc_englihoo.DataClass.Teacher
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * UserPreferences - Quản lý lưu trữ thông tin đăng nhập
 */
class UserPreferences(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    
    private val gson = Gson()
    
    companion object {
        private const val PREF_NAME = "ckc_englishoo_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_TYPE = "user_type"
        private const val KEY_STUDENT_DATA = "student_data"
        private const val KEY_TEACHER_DATA = "teacher_data"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_REMEMBER_LOGIN = "remember_login"
        
        const val USER_TYPE_STUDENT = "student"
        const val USER_TYPE_TEACHER = "teacher"
    }
    
    // ==================== LOGIN STATE ====================
    
    /**
     * Kiểm tra xem user đã đăng nhập chưa
     */
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * Lấy loại user hiện tại
     */
    fun getUserType(): String? {
        return sharedPreferences.getString(KEY_USER_TYPE, null)
    }
    
    /**
     * Kiểm tra có nhớ đăng nhập không
     */
    fun isRememberLogin(): Boolean {
        return sharedPreferences.getBoolean(KEY_REMEMBER_LOGIN, false)
    }
    
    // ==================== SAVE LOGIN DATA ====================
    
    /**
     * Lưu thông tin đăng nhập Student
     */
    fun saveStudentLogin(
        student: Student, 
        username: String, 
        password: String, 
        rememberLogin: Boolean = true
    ) {
        val editor = sharedPreferences.edit()
        
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_TYPE, USER_TYPE_STUDENT)
        editor.putString(KEY_STUDENT_DATA, gson.toJson(student))
        editor.putString(KEY_USERNAME, username)
        editor.putBoolean(KEY_REMEMBER_LOGIN, rememberLogin)
        
        if (rememberLogin) {
            editor.putString(KEY_PASSWORD, password)
        }
        
        editor.apply()
    }
    
    /**
     * Lưu thông tin đăng nhập Teacher
     */
    fun saveTeacherLogin(
        teacher: Teacher, 
        username: String, 
        password: String, 
        rememberLogin: Boolean = true
    ) {
        val editor = sharedPreferences.edit()
        
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_TYPE, USER_TYPE_TEACHER)
        editor.putString(KEY_TEACHER_DATA, gson.toJson(teacher))
        editor.putString(KEY_USERNAME, username)
        editor.putBoolean(KEY_REMEMBER_LOGIN, rememberLogin)
        
        if (rememberLogin) {
            editor.putString(KEY_PASSWORD, password)
        }
        
        editor.apply()
    }
    
    // ==================== GET LOGIN DATA ====================
    
    /**
     * Lấy thông tin Student đã lưu
     */
    fun getSavedStudent(): Student? {
        val studentJson = sharedPreferences.getString(KEY_STUDENT_DATA, null)
        return if (studentJson != null) {
            try {
                gson.fromJson(studentJson, Student::class.java)
            } catch (e: Exception) {
                null
            }
        } else null
    }
    
    /**
     * Lấy thông tin Teacher đã lưu
     */
    fun getSavedTeacher(): Teacher? {
        val teacherJson = sharedPreferences.getString(KEY_TEACHER_DATA, null)
        return if (teacherJson != null) {
            try {
                gson.fromJson(teacherJson, Teacher::class.java)
            } catch (e: Exception) {
                null
            }
        } else null
    }
    
    /**
     * Lấy username đã lưu
     */
    fun getSavedUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }
    
    /**
     * Lấy password đã lưu (chỉ khi remember login = true)
     */
    fun getSavedPassword(): String? {
        return if (isRememberLogin()) {
            sharedPreferences.getString(KEY_PASSWORD, null)
        } else null
    }
    
    // ==================== LOGOUT ====================
    
    /**
     * Đăng xuất - xóa tất cả thông tin đăng nhập
     */
    fun logout() {
        val editor = sharedPreferences.edit()
        
        editor.putBoolean(KEY_IS_LOGGED_IN, false)
        editor.remove(KEY_USER_TYPE)
        editor.remove(KEY_STUDENT_DATA)
        editor.remove(KEY_TEACHER_DATA)
        editor.remove(KEY_USERNAME)
        editor.remove(KEY_PASSWORD)
        editor.remove(KEY_REMEMBER_LOGIN)
        
        editor.apply()
    }
    
    /**
     * Đăng xuất nhưng giữ lại username (không giữ password)
     */
    fun logoutKeepUsername() {
        val username = getSavedUsername()
        logout()
        
        if (username != null) {
            val editor = sharedPreferences.edit()
            editor.putString(KEY_USERNAME, username)
            editor.apply()
        }
    }
    
    // ==================== UPDATE DATA ====================
    
    /**
     * Cập nhật thông tin Student
     */
    fun updateStudentData(student: Student) {
        if (getUserType() == USER_TYPE_STUDENT) {
            val editor = sharedPreferences.edit()
            editor.putString(KEY_STUDENT_DATA, gson.toJson(student))
            editor.apply()
        }
    }
    
    /**
     * Cập nhật thông tin Teacher
     */
    fun updateTeacherData(teacher: Teacher) {
        if (getUserType() == USER_TYPE_TEACHER) {
            val editor = sharedPreferences.edit()
            editor.putString(KEY_TEACHER_DATA, gson.toJson(teacher))
            editor.apply()
        }
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Xóa tất cả dữ liệu
     */
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
    
    /**
     * Kiểm tra có dữ liệu đăng nhập tự động không
     */
    fun hasAutoLoginData(): Boolean {
        return isLoggedIn() && isRememberLogin() && 
               getSavedUsername() != null && getSavedPassword() != null
    }
    
    /**
     * Lấy thông tin đăng nhập cho auto login
     */
    data class AutoLoginData(
        val username: String,
        val password: String,
        val userType: String
    )
    
    fun getAutoLoginData(): AutoLoginData? {
        return if (hasAutoLoginData()) {
            AutoLoginData(
                username = getSavedUsername()!!,
                password = getSavedPassword()!!,
                userType = getUserType()!!
            )
        } else null
    }
}
