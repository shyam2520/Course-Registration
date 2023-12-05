import { z } from "zod";

export const LoginForm = z.object({
    email: z.string().email({ message: "Please enter a valid email" }),
    password: z.string().min(8, { message: "Password must be at least 8 characters long" }),
});
  
export type LoginFormType = z.infer<typeof LoginForm>;