import { BookCopy } from "lucide-react";
import { ModeToggle } from "./ModeToggle";
import Signout from "./Signout";

export default function Navbar() {
  return (
    <nav className="h-16 border-b-2 sticky">
      <div className="h-full flex items-center justify-between container mx-auto">
        <div>
          <BookCopy size={32} />
        </div>
        <div className="flex items-center space-x-2">
          <ModeToggle />
          <Signout />
          {/* TODO: User Icon component with drop menu for logout */}
          {/* <User /> */}
        </div>
      </div>
    </nav>
  )
}
