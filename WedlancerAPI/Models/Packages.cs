using Newtonsoft.Json;
using System;
using System.Collections.Generic;

// Code scaffolded by EF Core assumes nullable reference types (NRTs) are not used or disabled.
// If you have enabled NRTs for your project, then un-comment the following line:
// #nullable disable

namespace WedlancerAPI.Models
{
    public partial class Packages
    {
        public Packages()
        {
            Orders = new HashSet<Orders>();
        }

        public int PackageId { get; set; }
        public string PackageName { get; set; }
        public string Description { get; set; }
        public double Price { get; set; }

        [JsonIgnore]
        public virtual ICollection<Orders> Orders { get; set; }
    }
}
