import React, { useRef, useEffect, useState } from "react";
import * as d3 from "d3";

export const BoxPlot = ({ title, data }) => {
  const ref = useRef();
  const divRef = useRef();

  useEffect(() => {
    if (divRef.current &&  data.max) {
      var margin = { top: 0, right: 30, bottom: 5, left: 40 },
        width = divRef.current.offsetWidth - margin.left - margin.right,
        height = 125;
      const minI = data.min < 0 ? data.min - 5 : -5;
      const maxI = data.max + 5;
      var y = d3.scaleLinear().domain([minI, maxI]).range([width, 0]);
      const svg = d3
        .select(ref.current)
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
      svg
        .append("g")
        .attr("transform", "translate(0," + 100 + ")")
        .call(d3.axisBottom(y));
      var center = 50;
      width = 50;
      svg
        .append("line")
        .attr("y1", center)
        .attr("y2", center)
        .attr("x1", y(data.min))
        .attr("x2", y(data.max))
        .attr("stroke", "#1C3978");

      // Show the box
      svg
        .append("rect")
        .attr("y", center - width / 2)
        .attr("x", y(data.q3))
        .attr("width", y(data.q1) - y(data.q3))
        .attr("height", width)
        .attr("stroke", "#1C3978")
        .style("fill", "#fff");
      svg
        .selectAll("toto")
        .data([data.min, data.median, data.max])
        .enter()
        .append("line")
        .attr("y1", center - width / 2)
        .attr("y2", center + width / 2)
        .attr("x1", function (d) {
          return y(d);
        })
        .attr("x2", function (d) {
          return y(d);
        })
        .attr("stroke", "#1C3978");
        return () => {
          svg.remove();
        }
    }
  }, [data, divRef.current?.offsetWidth]);
  return (
    <div calssName="justify-content-center" ref={divRef} className="d-flex flex-column">
      <p>{title}</p>
      {divRef.current?.offsetWidth && (
        <svg ref={ref} />
      )}
    </div>
  );
}
