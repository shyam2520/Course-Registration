import React from "react";
import { Navigate } from "react-router-dom";

interface ProtectedRouteProps {
  token: string;
  children: React.ReactNode;
}

export default function ProtectedRoute({ token, children }: ProtectedRouteProps) {


  if (!token) {
    return <Navigate to="/signin" />
  }

  return (
    children
  )
}
