import { useAuth } from "@/providers/AuthProvider";
import { useEffect } from "react";


export default function Dashboard() {

  const { user } = useAuth();

  useEffect(() => {
    console.log(user)
  }, [user])
  
  return (
    <div>Dashboard</div>
  )
}
