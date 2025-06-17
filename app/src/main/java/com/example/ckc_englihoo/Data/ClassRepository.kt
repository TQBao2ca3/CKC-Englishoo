package com.example.ckc_englihoo.Data

import androidx.compose.ui.graphics.Color
import com.example.ckc_englihoo.R

object ClassRepository {

    // Dữ liệu các lớp học (giống với HomeScreenTeacher) - sử dụng mutableList để có thể thêm mới
    private val _teacherClasses = mutableListOf(
        TeacherClass(
            id = "DNTN",
            name = "ĐNTN",
            description = "Khóa 22 Năm 2024-2025",
            academicYear = "2024-2025",
            studentCount = 12,
            backgroundColor = Color(0xFFE57373)
        )
        // Comment out other classes để test first class welcome message
        /*,
        TeacherClass(
            id = "TTTN_CD_TH_22",
            name = "TTTN CĐ TH 22",
            description = "HK2 Năm học: 2024-2025",
            academicYear = "2024-2025",
            studentCount = 6,
            backgroundColor = Color(0xFF5C6BC0)
        ),
        TeacherClass(
            id = "TH_CD_CNTT_24B_MMT",
            name = "TH CĐ CNTT 24B MMT",
            description = "HK2 Năm học: 2024-2025",
            academicYear = "2024-2025",
            studentCount = 0,
            backgroundColor = Color(0xFF8D6E63)
        ),
        TeacherClass(
            id = "CD_TH_23E_HQTCSDL",
            name = "CĐ TH 23E - HQTCSDL",
            description = "HK1 Năm học: 2024-2025",
            academicYear = "2024-2025",
            studentCount = 72,
            backgroundColor = Color(0xFFFF8A65)
        ),
        TeacherClass(
            id = "CD_TH_22DDD_LTDD",
            name = "CĐ TH 22DDD - LTDD",
            description = "HK1 Năm học 2024-2025",
            academicYear = "2024-2025",
            studentCount = 58,
            backgroundColor = Color(0xFF4FC3F7)
        )
        */
    )
    
    // Public read-only access
    val teacherClasses: List<TeacherClass> get() = _teacherClasses.toList()

    /**
     * Tìm class theo ID
     */
    fun getClassById(classId: String): TeacherClass? {
        return _teacherClasses.find { it.id == classId }
    }

    /**
     * Lấy tất cả classes
     */
    fun getAllClasses(): List<TeacherClass> {
        return _teacherClasses.toList()
    }

    /**
     * Thêm class mới
     */
    fun addClass(newClass: TeacherClass): Boolean {
        return try {
            // Kiểm tra xem class ID đã tồn tại chưa
            if (_teacherClasses.any { it.id == newClass.id }) {
                false // Class đã tồn tại
            } else {
                _teacherClasses.add(0, newClass) // Thêm vào đầu danh sách
                true // Thành công
            }
        } catch (e: Exception) {
            false // Lỗi
        }
    }

    /**
     * Xóa class theo ID
     */
    fun removeClass(classId: String): Boolean {
        return try {
            _teacherClasses.removeAll { it.id == classId }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Xóa class theo ID (alias for removeClass)
     */
    fun deleteClass(classId: String): Boolean {
        return removeClass(classId)
    }

    /**
     * Cập nhật tên class
     */
    fun updateClassName(classId: String, newName: String): Boolean {
        return try {
            val classIndex = _teacherClasses.indexOfFirst { it.id == classId }
            if (classIndex != -1) {
                val currentClass = _teacherClasses[classIndex]
                _teacherClasses[classIndex] = currentClass.copy(name = newName)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Cập nhật mô tả class
     */
    fun updateClassDescription(classId: String, newDescription: String): Boolean {
        return try {
            val classIndex = _teacherClasses.indexOfFirst { it.id == classId }
            if (classIndex != -1) {
                val currentClass = _teacherClasses[classIndex]
                _teacherClasses[classIndex] = currentClass.copy(description = newDescription)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Cập nhật thông tin class
     */
    fun updateClass(updatedClass: TeacherClass): Boolean {
        return try {
            val index = _teacherClasses.indexOfFirst { it.id == updatedClass.id }
            if (index != -1) {
                _teacherClasses[index] = updatedClass
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    // Posts storage for each class
    private val _classPosts = mutableMapOf<String, MutableList<ClassPost>>()

    /**
     * Lấy posts của một class
     */
    fun getClassPosts(classId: String): List<ClassPost> {
        return _classPosts[classId] ?: emptyList()
    }

    /**
     * Thêm post mới vào class
     */
    fun addPostToClass(classId: String, post: ClassPost) {
        if (_classPosts[classId] == null) {
            _classPosts[classId] = mutableListOf()
        }
        _classPosts[classId]?.add(0, post) // Add to beginning of list
    }

    /**
     * Xóa post khỏi class
     */
    fun deletePostFromClass(classId: String, postId: String) {
        _classPosts[classId]?.removeAll { it.id == postId }
    }

    /**
     * Cập nhật post trong class
     */
    fun updatePostInClass(classId: String, updatedPost: ClassPost) {
        _classPosts[classId]?.let { posts ->
            val index = posts.indexOfFirst { it.id == updatedPost.id }
            if (index != -1) {
                posts[index] = updatedPost
            }
        }
    }

    /**
     * Lấy post theo ID
     */
    fun getPostById(classId: String, postId: String): ClassPost? {
        return _classPosts[classId]?.find { it.id == postId }
    }

    /**
     * Convert LegacyClassPost to ClassPost
     */
    fun convertLegacyPost(legacyPost: LegacyClassPost): ClassPost {
        return ClassPost(
            id = legacyPost.id,
            authorName = "Mr. Nhâm Chí Bửu",
            authorAvatar = R.drawable.teacher,
            content = "${legacyPost.title}\n\n${legacyPost.content}",
            timestamp = legacyPost.timestamp,
            comments = emptyList(),
            attachments = legacyPost.attachments.map { attachment ->
                AttachmentItem(
                    id = System.currentTimeMillis().toString(),
                    name = attachment,
                    type = AttachmentType.FILE
                )
            }
        )
    }


}
