
import Navbar from "./Navbar";
// import Sidebar from "./Sidebar";
import Table from "./CourseTable/Table";
import { courseTable } from "@/types/courseTable";
import { useEffect, useState } from "react";
import { useToken } from "@/store/AuthStore";
import axios from "axios";
import { useMutation } from "react-query";
import Sidebar from "./Sidebar";
import { course } from "@/types/course";

export default function Courseregister() {

  const token = useToken();
  const [tableData, setTableData] = useState<courseTable[]>([])
  
  const {mutate: getTableData} = useMutation({
    mutationFn: async () => {
      const { data } = await axios.get(import.meta.env.VITE_SPRING_URL + "/api/courses/getAllCourses", {
        headers: {
          Authorization: `Bearer ${token}`,
        }
      })
      return data
    },
    onError: (error) => {
      console.log(error)
    },
    onSuccess: (data) => { 
      console.log(data)
      const courses: courseTable[] = data.map((course: course) => {
        return {
          title: course.title,
          crn: course.crn,
          semester: course.semester,
          hours: course.hours,
          enrolled: course.enrollment,
          seats: course.seats,
          instructor: course.instructor,
          time: "Thursday 9:00 AM - 10:00 AM",
          prerequisites: course.prerequisite.map((prereq) => parseInt(prereq)),
        }
      })
      setTableData(courses)
    }
  })

  useEffect(() => {
    // get courses
    getTableData()
  }, [getTableData]);

  
  return (
    <div className="h-screen">
      <Navbar/>
      <div className="flex h-full">
        <Sidebar />
        <Table data={tableData} from="register"/>
      </div>
    </div>
  )
}
