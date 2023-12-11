import { useState, useEffect } from "react";
import { AgGridReact } from "ag-grid-react";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";

const DynamicAgGrid = ({ data }: { data: string[] }) => {
  const [rowData, setRowData] = useState<{ id: number; value: string }[]>([]);
  const [columnDefs, setColumnDefs] = useState<any[]>([]);
  const [paginationPageSize, setPaginationPageSize] = useState(10); // Set your desired page size

  useEffect(() => {
    if (data && data.length > 0) {
      // Extract the first element as row data
      const [row, ...columns] = data;

      // Update row data
      setRowData([{ id: 1, value: row }]);

      // Update column definitions based on the remaining elements
      const newColumnDefs = columns.map((col: any, index: number) => ({
        headerName: col,
        field: `value${index + 1}`,
        sortable: true,
        filter: true,
      }));

      setColumnDefs(newColumnDefs);
    }
  }, [data]);

  return (
    <div>
      {/* ag-Grid React component */}
      <div
        className="ag-theme-alpine"
        style={{ height: "300px", width: "100%" }}
      >
        <AgGridReact
          rowData={rowData}
          columnDefs={columnDefs}
          domLayout="autoHeight"
          pagination={true}
          paginationPageSize={paginationPageSize}
        />
      </div>
    </div>
  );
};

export default DynamicAgGrid;

// const initialData = ['Row 1', 'Column 1', 'Column 2', 'Column 3'];
//  <DynamicAgGrid data={initialData} />
