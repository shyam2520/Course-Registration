import Dashboard from "@/components/Dashboard"
import Signin from "@/components/Signin"
import Signup from "@/components/Signup"
import { RouterProvider, createBrowserRouter } from "react-router-dom"


export const Route = () => {

  const router = createBrowserRouter([
    {
      path: "/",
      element: <Dashboard />,
    },
    {
      path: "/signin",
      element: <Signin />,
    },
    {
      path: "/signup",
      element: <Signup />,
    },
  ]
  );

  return (
    <RouterProvider router={router} />
  )
}

