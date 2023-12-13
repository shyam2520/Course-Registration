import { useUser } from "@/store/AuthStore";
import { Link } from "react-router-dom";
import { Button } from "./ui/button";

export default function Sidebar() {

  const user = useUser();

  return (
    <div className="w-52 h-full flex flex-col p-2 space-y-2 border-r-2">
      <Button variant={'ghost'} className="justify-start">
        <Link to="/">Course Register</Link>
      </Button>
      <Button variant={'ghost'} className="justify-start">
        <Link to={`/${user.name}`}>Your Courses</Link>
      </Button>
    </div>
  )
}
