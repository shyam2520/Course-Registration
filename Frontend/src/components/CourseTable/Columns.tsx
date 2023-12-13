import { ColumnDef } from "@tanstack/react-table";
import { courseTable } from "@/types/courseTable";
import { Checkbox } from "../ui/checkbox";


export const columns: ColumnDef<courseTable>[] = [
  {
    id: "select",
    header: ({ table }) => (
      <Checkbox
        checked={
          table.getIsAllPageRowsSelected() ||
          (table.getIsSomePageRowsSelected() && "indeterminate")
        }
        onCheckedChange={(value) => table.toggleAllPageRowsSelected(!!value)}
        aria-label="Select all"
      />
    ),
    cell: ({ row }) => (
      <Checkbox
        checked={row.getIsSelected()}
        onCheckedChange={(value) => row.toggleSelected(!!value)}
        aria-label="Select row"
      />
    ),
  },
  {
    accessorKey: "title",
    header: "Title",
  },
  {
    accessorKey: "crn",
    header: "CRN",
  },
  {
    accessorKey: "semester",
    header: "Semester",
  },
  {
    accessorKey: "hours",
    header: "Hours",
  },
  {
    accessorKey: "enrolled",
    header: "Enrolled",
    cell: ({ row }) => {
      const { enrolled, seats } = row.original;

      return (
        <div className={`flex ${enrolled > 90 ? "text-red-500" : "text-green-500"}`}>
          <span>{enrolled}</span>
          <span className="mx-1"> of </span>
          <span>{seats}</span>
        </div>
      )
    }
  },
  {
    accessorKey: "prerequisites",
    header: "Prerequisites",
  },
  {
    accessorKey: "instructor",
    header: "Instructor",
  },
  {
    accessorKey: "time",
    header: "Time",
  },
] 