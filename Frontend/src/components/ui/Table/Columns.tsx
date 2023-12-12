import { ColumnDef } from "@tanstack/react-table"

// this is an example - we need to update this based on the api design

// This type is used to define the shape of our data.
// You can use a Zod schema here if you want.
export type Payment = {
  id: string
  amount: number
  status: "pending" | "processing" | "success" | "failed"
  email: string
}

export const columns: ColumnDef<Payment>[] = [
  {
    accessorKey: "title",
    header: "Title",
  },
  {
    accessorKey: "subjectDesc",
    header: "Subject description",
  },
  {
    accessorKey: "CourseNo",
    header: "Course Number",
  },
]
