
import Navbar from "./Navbar";
// import Sidebar from "./Sidebar";
import Table from "./CourseTable/Table";
import { course } from "@/types/course";
import { useEffect, useState } from "react";
import { useToken } from "@/store/AuthStore";
import axios from "axios";
import { useMutation } from "react-query";

export default function Dashboard() {

  const token = useToken();
  const [tableData, setTableData] = useState<course[]>([{
    id: "1",
    title: "test",
    crn: 6305,
    semester: "Fall",
    hours: 3,
    enrolled: 0,
    seats: 30,
    instructor: "test",
    time: "test",
    prerequisites: ["test"]
  }, {
    id: "1",
    title: "test",
    crn: 6200,
    semester: "Fall",
    hours: 3,
    enrolled: 0,
    seats: 30,
    instructor: "test",
    time: "test",
    prerequisites: ["test"]
  }])
  
  const {mutate: getTableData, isLoading} = useMutation({
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
    }
  })

  useEffect(() => {
    // get courses
    getTableData()
  }, []);

  
  return (
    <div>
      <Navbar/>
      <div>
        {/* <Sidebar /> */}
        <Table data={tableData} />
        {/* Main section component */}
      </div>
    </div>
  )
}
