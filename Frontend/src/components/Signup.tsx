import { SignupForm, SignupFormType } from "@/lib/validators/signup";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "./ui/form";
import { Input } from "./ui/input";
import { Button } from "./ui/button";
import { useMutation } from "react-query";
import axios from "axios";


export default function Signup() {

  const form = useForm<SignupFormType>({
    resolver: zodResolver(SignupForm),
    defaultValues: {
      name: "",
      email: "",
      password: "",
      role: ["user"],
      branch: "",
      degree: "",
    },
  });

  const {mutate: signUp, isLoading } = useMutation({
    mutationFn: async ({name, email, password, role, branch, degree}: SignupFormType) => {
      const payload = {
        name,
        email,
        password,
        role,
        branch,
        degree,
      };
      const { data } = await axios.post(import.meta.env.VITE_SPRING_URL + "/api/auth/signup", payload);
      return data;
    },
    onError: (error) => {
      console.log(error);
    },
    onSuccess: (data) => {
      console.log(data);
    }
  });
  

  return (
    <div className="flex flex-col max-w-3xl mx-auto h-screen justify-center items-center">
      <h1 className="text-2xl font-semibold">Sign Up</h1>
      <Form {...form}>
        <form onSubmit={form.handleSubmit((e) => signUp(e))} className="sm:w-96 w-64 space-y-6">
          <FormField
            control={form.control}
            name="name"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Name</FormLabel>
                <FormControl>
                  <Input placeholder="John Doe" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Email</FormLabel>
                <FormControl>
                  <Input placeholder="example@northeastern.edu" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="password"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Password</FormLabel>
                <FormControl>
                  <Input type="password" placeholder="password" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="branch"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Branch</FormLabel>
                <FormControl>
                  <Input type="branch" placeholder="CSE" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="degree"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Degree</FormLabel>
                <FormControl>
                  <Input type="degree" placeholder="B.Tech" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <Button type="submit" disabled={isLoading}>Sign Up</Button>
        </form>
      </Form>
    </div>
  )
}
