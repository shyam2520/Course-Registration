/* eslint-disable react-refresh/only-export-components */
import { AuthContextType } from "@/types/auth";
import { User } from "@/types/user";
import axios from "axios";
import { createContext, useContext, useEffect, useMemo, useState } from "react";

type AuthProviderProps = {
  children: React.ReactNode,
};

const AuthContext = createContext<AuthContextType>({token: "", user: { id: "", name: "", email: "", role: null }, setAuthData: () => {}});

export default function AuthProvider({
  children,
}: AuthProviderProps) {
  
  const [authData, setAuthData] = useState<{token: string, user:User}>({ token: sessionStorage.getItem("token") || "", user: sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user") || "") : { id: "", name: "", email: "", role: null }});
  
  useEffect(() => {
    if (authData.token) {
      axios.defaults.headers.common["Authorization"] = "Bearer " + authData.token;
      sessionStorage.setItem("token", authData.token);
      sessionStorage.setItem("user", JSON.stringify(authData.user));
    }
    else {
      delete axios.defaults.headers.common["Authorization"];
      sessionStorage.removeItem("token");
      sessionStorage.removeItem("user");
    }
  }, [authData])
  

  const contextValue: AuthContextType = useMemo(
    () => ({
      token: authData.token,
      user: authData.user,
      setAuthData,
    }),
    [authData.token, authData.user]
  );

  return (
    <AuthContext.Provider value={contextValue}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => {
  return useContext(AuthContext);
};

