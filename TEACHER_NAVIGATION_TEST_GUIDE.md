# 🧪 HƯỚNG DẪN TEST NAVIGATION TEACHER SCREENS

## 📱 **Cách chạy app và test navigation:**

### **Bước 1: Chạy app**
```bash
.\gradlew.bat installDebug
```
Hoặc chạy từ Android Studio

### **Bước 2: Khởi động app trên device/emulator**

---

## 🎯 **FLOW NAVIGATION TEACHER - CHI TIẾT**

### **1️⃣ OnboardingScreen → TeacherLogin**
- **Màn hình:** OnboardingScreen (splash + role selection)
- **Thao tác:** Bấm nút **"Bắt đầu"** → Chọn **"Giảng viên"**
- **Kết quả:** Chuyển đến TeacherLogin
- **File:** `RoleSelectionScreen.kt` → `LoginForm.kt`

### **2️⃣ TeacherLogin → HomeScreenTeacher**
- **Màn hình:** TeacherLogin (form đăng nhập)
- **Thao tác:** Nhập bất kỳ username/password → Bấm **"Đăng nhập"**
- **Kết quả:** Chuyển đến HomeScreenTeacher (dashboard)
- **File:** `LoginForm.kt` → `HomeScreenTeacher.kt`

### **3️⃣ HomeScreenTeacher → MyProfileTeacher**
- **Màn hình:** HomeScreenTeacher (dashboard với class cards)
- **Thao tác:** Bấm **avatar tròn ở góc phải** (next to "More options")
- **Kết quả:** Chuyển đến MyProfileTeacher
- **File:** `HomeScreenTeacher.kt` line 124: `onProfileClick()`

### **4️⃣ MyProfileTeacher → HomeScreenTeacher**
- **Màn hình:** MyProfileTeacher (hồ sơ giảng viên)
- **Thao tác:** Bấm **settings icon** → Chọn **"Quản lý lớp học"**
- **Kết quả:** Quay về HomeScreenTeacher
- **File:** `MyProfileTeacher.kt` dropdown menu

### **5️⃣ HomeScreenTeacher → ClassDetailTeacher**
- **Màn hình:** HomeScreenTeacher
- **Thao tác:** Bấm vào **bất kỳ class card nào** (ĐNTN, TTTN CĐ TH 22, etc.)
- **Kết quả:** Chuyển đến ClassDetailTeacher với thông tin class
- **File:** `HomeScreenTeacher.kt` → `ClassDetailTeacher.kt`

### **6️⃣ ClassDetailTeacher → CreatePostTeacher**
- **Màn hình:** ClassDetailTeacher (có 4 tabs: Bảng tin, Bài tập, Học sinh, Điểm số)
- **Thao tác:** Bấm **FAB (+) ở góc dưới phải**
- **Kết quả:** Chuyển đến CreatePostTeacher
- **File:** `ClassDetailTeacher.kt` → `CreatePostTeacher.kt`

### **7️⃣ ClassDetailTeacher → StudentListTeacher**
- **Màn hình:** ClassDetailTeacher
- **Thao tác:** Bấm **tab "Học sinh"**
- **Kết quả:** Chuyển đến StudentListTeacher
- **File:** `ClassDetailTeacher.kt` → `StudentListTeacher.kt`

### **8️⃣ ClassDetailTeacher → GradesTeacher**
- **Màn hình:** ClassDetailTeacher
- **Thao tác:** Bấm **tab "Điểm số"**
- **Kết quả:** Chuyển đến GradesTeacher
- **File:** `ClassDetailTeacher.kt` → `GradesTeacher.kt`

### **9️⃣ Back Navigation**
- **Từ bất kỳ màn hình nào:** Bấm **nút back** (←) ở góc trái
- **Kết quả:** Quay về màn hình trước đó
- **Hoặc:** Sử dụng system back button

---

## 🔍 **KIỂM TRA CỤ THỂ**

### **✅ Những gì cần hoạt động:**

#### **HomeScreenTeacher:**
- [ ] **Profile avatar** (góc phải) → MyProfileTeacher
- [ ] **Class cards** → ClassDetailTeacher
- [ ] **More options** (3 dots) → Menu dropdown

#### **MyProfileTeacher:**
- [ ] **Settings icon** → Dropdown menu
- [ ] **"Quản lý lớp học"** → HomeScreenTeacher
- [ ] **"Đăng xuất"** → OnboardingScreen

#### **ClassDetailTeacher:**
- [ ] **Back button** → HomeScreenTeacher
- [ ] **FAB (+)** → CreatePostTeacher
- [ ] **Tab "Học sinh"** → StudentListTeacher
- [ ] **Tab "Điểm số"** → GradesTeacher

#### **CreatePostTeacher:**
- [ ] **Back button** → ClassDetailTeacher
- [ ] **"Đăng bài" button** → ClassDetailTeacher

#### **StudentListTeacher:**
- [ ] **Back button** → ClassDetailTeacher
- [ ] **FAB (+)** → Invite student (TODO)

#### **GradesTeacher:**
- [ ] **Back button** → ClassDetailTeacher
- [ ] **Add assignment** → Dialog (TODO)

---

## 🚨 **NẾU NAVIGATION KHÔNG HOẠT ĐỘNG:**

### **Kiểm tra:**
1. **Build thành công chưa?** → `.\gradlew.bat assembleDebug`
2. **App đã install chưa?** → `.\gradlew.bat installDebug`
3. **Có crash không?** → Xem logcat
4. **Bấm đúng nút chưa?** → Theo hướng dẫn trên

### **Debug:**
1. **Xem logcat:** `adb logcat | grep -i "navigation\|error"`
2. **Check build errors:** Xem console output
3. **Restart app:** Force close và mở lại

---

## 📋 **CHECKLIST TEST HOÀN CHỈNH:**

- [ ] OnboardingScreen → TeacherLogin
- [ ] TeacherLogin → HomeScreenTeacher  
- [ ] HomeScreenTeacher → MyProfileTeacher
- [ ] MyProfileTeacher → HomeScreenTeacher
- [ ] HomeScreenTeacher → ClassDetailTeacher
- [ ] ClassDetailTeacher → CreatePostTeacher
- [ ] ClassDetailTeacher → StudentListTeacher
- [ ] ClassDetailTeacher → GradesTeacher
- [ ] All back buttons work
- [ ] Logout works
- [ ] No crashes

**Nếu tất cả đều ✅ → Navigation hoạt động hoàn hảo!**
