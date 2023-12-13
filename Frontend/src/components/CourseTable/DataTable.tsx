import { ColumnDef, flexRender, getCoreRowModel, getPaginationRowModel, useReactTable } from "@tanstack/react-table";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "../ui/table";
import { Button } from "../ui/button";
import { useState } from "react";
import { useMutation } from "react-query";
import { courseTable } from "@/types/courseTable";
import { CourseRequestType } from "@/lib/validators/coursrequest";
import { useToken } from "@/store/AuthStore";
import axios from "axios";
import { useRemoveCourse } from "@/store/CoureseStore";


interface DataTableProps<TData, TValue> {
  columns: ColumnDef<TData, TValue>[],
  data: TData[],
  from: string,
}
export default function DataTable<TData, TValue>({
  columns,
  data,
  from,
}: DataTableProps<TData, TValue>) {

  const token = useToken();
  const removeCourse = useRemoveCourse();
  const [rowSelection, setRowSelection] = useState({})
  
  const table = useReactTable({
    data,
    columns,
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    onRowSelectionChange: setRowSelection,
    state: {
      rowSelection
    },
  })

  const {mutate: registerCourse} = useMutation({
    mutationFn: async () => {
      const payload: CourseRequestType = {
        courseCRNS: table.getFilteredSelectedRowModel().rows.map((row) => {
          const course: courseTable = row.original as courseTable
          return course.crn
        })
      }

      const { data } = await axios.post(import.meta.env.VITE_SPRING_URL + "/api/courses/register", payload, {
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

  const {mutate: dropCourse} = useMutation({
    mutationFn: async () => {
      const payload: CourseRequestType = {
        courseCRNS: table.getFilteredSelectedRowModel().rows.map((row) => {
          const course: courseTable = row.original as courseTable
          return course.crn
        })
      }

      const { data } = await axios.post(import.meta.env.VITE_SPRING_URL + "/api/courses/drop", payload, {
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
      table.getFilteredSelectedRowModel().rows.forEach((row) => {
        const course: courseTable = row.original as courseTable
        removeCourse(course.crn)
      })
    }
  })


  return (
    <div>
      <div className="flex items-center justify-end pb-4">
        {
          from === 'register' ? 
            (<Button variant={'default'} onClick={() => registerCourse()}>
              Register
            </Button>)
            :
            (<Button variant={'default'} onClick={() => dropCourse()}>
              Drop
            </Button>)
        }
        
      </div>
      <div className="rounded-md border">
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header) => {
                  return (
                    <TableHead key={header.id}>
                      {header.isPlaceholder
                        ? null
                        : flexRender(
                            header.column.columnDef.header,
                            header.getContext()
                          )}
                    </TableHead>
                  )
                })}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map((row) => (
                <TableRow
                  key={row.id}
                  data-state={row.getIsSelected() && "selected"}
                >
                  {row.getVisibleCells().map((cell) => (
                    <TableCell key={cell.id}>
                      {flexRender(cell.column.columnDef.cell, cell.getContext())}
                    </TableCell>
                  ))}
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={columns.length} className="h-24 text-center">
                  No results.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <div className="flex justify-between items-center py-4">
        <div className="flex-1 text-sm text-muted-foreground ml-2">
          {table.getFilteredSelectedRowModel().rows.length} of{" "}
          {table.getFilteredRowModel().rows.length} row(s) selected.
        </div>
        <div className="flex items-center justify-end space-x-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => table.previousPage()}
            disabled={!table.getCanPreviousPage()}
          >
            Previous
          </Button>
          <Button
            variant="outline"
            size="sm"
            onClick={() => table.nextPage()}
            disabled={!table.getCanNextPage()}
          >
            Next
          </Button>
        </div>
      </div>
    </div>
  )
}
