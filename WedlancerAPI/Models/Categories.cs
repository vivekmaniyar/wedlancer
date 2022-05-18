using Newtonsoft.Json;
using System;
using System.Collections.Generic;


// Code scaffolded by EF Core assumes nullable reference types (NRTs) are not used or disabled.
// If you have enabled NRTs for your project, then un-comment the following line:
// #nullable disable

namespace WedlancerAPI.Models
{
    public partial class Categories
    {
        public Categories()
        {
            Profiles = new HashSet<Profiles>();
        }

        public int CategoryId { get; set; }
        public string CategoryName { get; set; }

        [JsonIgnore]
        public virtual ICollection<Profiles> Profiles { get; set; }
    }
}
