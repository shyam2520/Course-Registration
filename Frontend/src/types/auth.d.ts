export type AuthContextType = {
  token: string;
  user: User;
  setAuthData: (data: { token: string; user: User; }) => void;
};
