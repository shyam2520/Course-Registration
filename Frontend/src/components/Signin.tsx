import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "./ui/form";
import { Input } from "./ui/input";
import { Button } from "./ui/button";
import { SigninForm, SigninFormType } from "@/lib/validators/signin";
import axios from "axios";
import { useMutation } from "react-query";


export default function Signin() {

  const form = useForm<SigninFormType>({
    resolver: zodResolver(SigninForm),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const {mutate: signIn, isLoading } = useMutation({
    mutationFn: async ({email, password}: SigninFormType) => {
      const payload = {
        email,
        password,
      };
      const { data } = await axios.post(import.meta.env.VITE_SPRING_URL + "/api/auth/signin", payload);
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
      <h1 className="text-2xl font-semibold">Sign In</h1>
      <Form {...form}>
        <form onSubmit={form.handleSubmit((e) => signIn(e))} className="sm:w-96 w-64 space-y-6">
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
          <Button type="submit" disabled={isLoading}>Sign In</Button>
        </form>
      </Form>
    </div>
  )
}
