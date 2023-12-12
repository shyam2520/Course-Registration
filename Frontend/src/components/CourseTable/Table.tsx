import { course } from "@/types/course"
import { columns } from "./Columns"
import DataTable from "./DataTable"

interface tableProps {
  data: course[],
}

export default function Table({ data }: tableProps) {

  return (
    <div className="container mx-auto py-10">
      <DataTable columns={columns} data={data} />
    </div>
  )
}
