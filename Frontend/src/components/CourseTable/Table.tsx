import { courseTable } from "@/types/courseTable"
import { columns } from "./Columns"
import DataTable from "./DataTable"

interface tableProps {
  data: courseTable[],
  from: string,
}

export default function Table({ data, from }: tableProps) {

  return (
    <div className="container mx-auto py-10 overflow-auto">
      <DataTable columns={columns} data={data} from={from}/>
    </div>
  )
}
