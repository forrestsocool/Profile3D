package io.github.sm1314.profile3d.feature.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Employee implements Parcelable, Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;  // 员工编号
    private String name;  // 姓名
    private String sex;  // 性别
    private String nation;  // 民族
    private String location;  // 籍贯
    private String party;  // 政治面貌
    private Date armyday;  // 入伍年月
    private Date birthday;  // 生日
    private Date workday;  // 调入本单位时间
    private Department department;  // 部门
    private Position position;  // 职位
    private Integer dept_id;
    private Integer pos_id;

    public Integer getDept_id() {
        return dept_id;
    }

    public void setDept_id(Integer dept_id) {
        this.dept_id = dept_id;
    }

    public Integer getPos_id() {
        return pos_id;
    }

    public void setPos_id(Integer pos_id) {
        this.pos_id = pos_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public Date getArmyday() {
        return armyday;
    }

    public void setArmyday(Date armyday) {
        this.armyday = armyday;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Date getWorkday() {
        return workday;
    }

    public void setWorkday(Date workday) {
        this.workday = workday;
    }

    public String getAvatarUrl(String baseUrl, int size){
        String sizeStr = "";
        String sex = (getName()!=null && getName().equals("女")) ? "female" : "male";
        switch (size)
        {
            case 96:
                sizeStr = "96x96";
                break;
            case 128:
                sizeStr = "128x128";
                break;
            case 256:
                sizeStr = "256x256";
                break;
            case 512:
                sizeStr = "512x512";
                break;
            default:
                break;
        }
        return baseUrl + "/avatar/" + id + ".jpg?size=" + sizeStr + "&sex=" + sex;
    }

    @Override
    public String toString() {
        return "Employee:[id=" + id + ",name=" + name + ",sex=" + sex
                + ",nation=" + nation + ",location=" + location + ",party=" + party
                + ",armyday=" + armyday + ",birthday=" + birthday + ",workday=" + workday
                + ",department=" + department + ",position=" + position + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(sex);
        dest.writeString(nation);
        dest.writeString(location);
        dest.writeString(party);
        dest.writeParcelable(department, flags);
        dest.writeParcelable(position, flags);
        if (dept_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(dept_id);
        }
        if (pos_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(pos_id);
        }
        dest.writeLong(armyday != null ? armyday.getTime() : -1);
        dest.writeLong(birthday != null ? birthday.getTime() : -1);
        dest.writeLong(workday != null ? workday.getTime() : -1);
    }

    protected Employee(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        sex = in.readString();
        nation = in.readString();
        location = in.readString();
        party = in.readString();
        department = in.readParcelable(Department.class.getClassLoader());
        position = in.readParcelable(Position.class.getClassLoader());

        if (in.readByte() == 0) {
            dept_id = null;
        } else {
            dept_id = in.readInt();
        }
        if (in.readByte() == 0) {
            pos_id = null;
        } else {
            pos_id = in.readInt();
        }
        long tmpDate1 = in.readLong();
        armyday = tmpDate1 == -1 ? null : new Date(tmpDate1);
        long tmpDate2 = in.readLong();
        birthday = tmpDate2 == -1 ? null : new Date(tmpDate2);
        long tmpDate3 = in.readLong();
        workday = tmpDate3 == -1 ? null : new Date(tmpDate3);
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };
}