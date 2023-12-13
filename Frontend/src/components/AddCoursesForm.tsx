import { DialogTitle } from "@radix-ui/react-dialog";
import { Dialog, DialogClose, DialogContent, DialogFooter, DialogHeader, DialogTrigger } from "./ui/dialog";
import { Form, FormControl, FormField, FormItem, FormLabel } from "./ui/form";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { courseAdd, courseAddType } from "@/lib/validators/course";
import { useForm } from "react-hook-form";
import { Loader2 } from "lucide-react";
import { course } from "@/types/course";
import axios from "axios";
import { useToken } from "@/store/AuthStore";
import { useMutation } from "react-query";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select";
import { zodResolver } from "@hookform/resolvers/zod";
import moment from "moment";

export default function AddCoursesForm() {

  const token = useToken();

  const form = useForm<courseAddType>({
    resolver: zodResolver(courseAdd),
    defaultValues: {
      title: "",
      semester: "",
      hours: 4,
      crn: Math.floor(Math.random() * 100000),
      enrollment: 0,
      seats: 100,
      instructor: "",
      startTime: "",
      endTime: "",
      day: "",
    },
  })

  

  const { mutate: addCourse, isLoading } = useMutation({
    mutationFn: async ( { title, crn, semester, seats, enrollment, hours, instructor, startTime, endTime, day }: courseAddType ) => {
      const payload: course = {
        title: title,
        crn: crn,
        semester: semester,
        hours: hours,
        enrollment: enrollment,
        seats: seats,
        instructor: instructor,
        classTiming: {
          startTime: moment(startTime, "HH:mm").format("hh:mm A"),
          endTime: moment(endTime, "HH:mm").format("hh:mm A"),
          day: day,
        }
      }

      const { data } = await axios.post(import.meta.env.VITE_SPRING_URL + "/api/courses/add", {
        courses: [payload],
      }, {
        headers: {
          Authorization: `Bearer ${token}`,
        }
      })

      return data
    },
    onError: (error) => {
      console.log(error)
    },
    onSuccess: (data) => {
      console.log(data)
    }
  })

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button>Add Course</Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
            <DialogTitle>Add course</DialogTitle>
        </DialogHeader>
        <Form {...form}>
          <form onSubmit={form.handleSubmit((e) => addCourse(e))}>
            <div className="grid gap-4 grid-flow-col">
            <div className="space-y-4">
              <FormField
                control={form.control}
                name="title"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Title</FormLabel>
                    <FormControl>
                      <Input placeholder="Object Oriented Design" {...field} />
                    </FormControl>
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="semester"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Semester</FormLabel>
                    <FormControl>
                      <Input placeholder="Fall 2023" {...field} />
                    </FormControl>
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="hours"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Hours</FormLabel>
                    <FormControl>
                      <Input type="number" placeholder="4" {...field} />
                    </FormControl>
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="crn"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>CRN</FormLabel>
                    <FormControl>
                      <Input type="number" placeholder="12345" {...field} />
                    </FormControl>
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="seats"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Seats</FormLabel>
                    <FormControl>
                      <Input type="number" placeholder="100" {...field} />
                    </FormControl>
                  </FormItem>
                )}
              />
            </div>
            <div className="space-y-4">
              <FormField
                control={form.control}
                name="instructor"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Instructor</FormLabel>
                    <FormControl>
                      <Input placeholder="John Doe" {...field} />
                    </FormControl>
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="startTime"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Start Time</FormLabel>
                    <FormControl>
                      <Input type="time" {...field} />
                    </FormControl>
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="endTime"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>End Time</FormLabel>
                    <FormControl>
                      <Input type="time" {...field} />
                    </FormControl>
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="day"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Days</FormLabel>
                    <Select onValueChange={field.onChange} defaultValue={field.value}>
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select a day"/>
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value="Monday">Monday</SelectItem>
                        <SelectItem value="Tuesday">Tuesday</SelectItem>
                        <SelectItem value="Wednesday">Wednesday</SelectItem>
                        <SelectItem value="Thursday">Thursday</SelectItem>
                        <SelectItem value="Friday">Friday</SelectItem>
                      </SelectContent>
                    </Select>
                  </FormItem>
                )}
              />
            </div>
            </div>
            <DialogFooter>
              <DialogClose asChild>
                <Button type="submit" disabled={isLoading} className="mt-6">
                  {isLoading && <Loader2 size={18} className="mr-2 animate-spin"/>}
                  Add course
                </Button>
              </DialogClose>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  )
}
